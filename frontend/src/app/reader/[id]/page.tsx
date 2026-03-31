"use client";

import React, { useEffect, useMemo, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import {
  ArrowLeft,
  ZoomIn,
  ZoomOut,
  ChevronLeft,
  ChevronRight,
  Settings,
  Menu,
  X,
  Contrast,
  Brain,
  Eye,
} from "lucide-react";

import api from "@/services/api";
import { AccessibilityPanel } from "@/components/AccessibilityPanel";
import { Button } from "@/components/Button";

type AccessibilitySettings = {
  fontSize: number;
  lineSpacing: number;
  columnWidth: number;
  highContrast: boolean;
  focusMode: boolean;
  dyslexiaMode: boolean;
};

type ReadingResponseDTO = {
  documentTitle: string;
  lines: string[];
  currentPage: number;
  pageSize?: number;
  totalPages: number;
  totalLines?: number;
  lastPage?: boolean;
  hasNext: boolean;
  hasPrevious: boolean;
};

const FALLBACK_TOTAL_PAGES = 3;

const getFallbackLines = (page: number): string[] => {
  const pages: Record<number, string[]> = {
    1: [
      "Bem-vindo ao Sistema de Leitura Adaptativa.",
      "Este conteúdo de demonstração é exibido quando a API não está disponível ou quando o documento real ainda não foi carregado.",
      "A proposta do leitor é oferecer uma experiência mais próxima de um leitor moderno, com foco em conforto visual, atalhos e acessibilidade.",
      "Você pode testar zoom, alto contraste, modo dislexia, modo foco e navegação por páginas.",
      "Quando o backend estiver disponível, o conteúdo real será carregado automaticamente.",
    ],
    2: [
      "As regras de acessibilidade aplicadas aqui seguem a lógica do projeto.",
      "O alto contraste controla principalmente as cores.",
      "O modo dislexia controla tipografia, espaçamentos e alinhamento do texto.",
      "O modo foco simplifica a interface para reduzir distrações.",
      "Essas regras ajudam a demonstrar o comportamento esperado do sistema mesmo em modo offline.",
    ],
    3: [
      "O leitor foi estruturado para se aproximar visualmente de uma experiência semelhante ao Edge PDF Reader.",
      "A barra superior concentra navegação, zoom, ações rápidas e configurações avançadas.",
      "A área lateral exibe navegação por páginas.",
      "A área central prioriza leitura, contraste e adaptação do conteúdo.",
      "Assim, o sistema mantém coerência visual e utilidade prática no contexto acadêmico do projeto.",
    ],
  };

  return pages[page] ?? pages[1];
};

const buildFallbackReadingData = (page: number): ReadingResponseDTO => ({
  documentTitle: "Modo demonstração — Leitura Adaptativa",
  currentPage: page,
  totalPages: FALLBACK_TOTAL_PAGES,
  hasNext: page < FALLBACK_TOTAL_PAGES,
  hasPrevious: page > 1,
  lines: getFallbackLines(page),
});

export default function PDFReader() {
  const router = useRouter();
  const params = useParams();
  const id = params.id as string;

  const [currentPage, setCurrentPage] = useState(1);
  const [zoom, setZoom] = useState(100);
  const [showSidebar, setShowSidebar] = useState(false);
  const [showAccessibilityPanel, setShowAccessibilityPanel] = useState(false);
  const [isFallbackMode, setIsFallbackMode] = useState(false);

  const [settings, setSettings] = useState<AccessibilitySettings>(() => {
    if (typeof window === "undefined") {
      return {
        fontSize: 18,
        lineSpacing: 1.6,
        columnWidth: 78,
        highContrast: false,
        focusMode: false,
        dyslexiaMode: false,
      };
    }

    const savedSettings = localStorage.getItem("accessibility-settings");

    if (savedSettings) {
      try {
        return JSON.parse(savedSettings) as AccessibilitySettings;
      } catch {
        return {
          fontSize: 18,
          lineSpacing: 1.6,
          columnWidth: 78,
          highContrast: false,
          focusMode: false,
          dyslexiaMode: false,
        };
      }
    }

    return {
      fontSize: 18,
      lineSpacing: 1.6,
      columnWidth: 78,
      highContrast: false,
      focusMode: false,
      dyslexiaMode: false,
    };
  });

  const [readingData, setReadingData] = useState<ReadingResponseDTO | null>(
    buildFallbackReadingData(1)
  );
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadProfile() {
      try {
        const response = await api.get("/accessibility/profile/me");
        const data = response.data;

        setSettings((prev) => ({
          ...prev,
          fontSize: data.fontSize ?? prev.fontSize,
          lineSpacing: data.lineHeight ?? prev.lineSpacing,
          columnWidth: data.columnWidth ?? prev.columnWidth,
          highContrast: data.theme === "HIGH_CONTRAST",
          focusMode: data.focusMode ?? prev.focusMode,
          dyslexiaMode: data.dyslexiaFriendlyFont ?? prev.dyslexiaMode,
        }));
      } catch (err) {
        console.error("Erro ao carregar perfil de acessibilidade:", err);
      }
    }

    loadProfile();
  }, []);

  useEffect(() => {
    if (typeof window !== "undefined") {
      localStorage.setItem("accessibility-settings", JSON.stringify(settings));
    }
  }, [settings]);

  useEffect(() => {
    async function loadReading() {
      if (!id) return;

      try {
        setLoading(true);

        const response = await api.get(`/reading/${id}?page=${currentPage}`);
        setReadingData(response.data);
        setIsFallbackMode(false);
      } catch (err) {
        console.error("Erro ao carregar documento:", err);
        setReadingData(buildFallbackReadingData(currentPage));
        setIsFallbackMode(true);
      } finally {
        setLoading(false);
      }
    }

    loadReading();
  }, [id, currentPage]);

  const totalPages = readingData?.totalPages ?? FALLBACK_TOTAL_PAGES;
  const documentName = readingData?.documentTitle || `Documento ${id}`;
  const isSidebarVisible = showSidebar && !settings.focusMode;

  const previewPages = useMemo(() => {
    return Array.from({ length: totalPages }, (_, index) => index + 1);
  }, [totalPages]);

  const handleZoomIn = () => {
    setZoom((prev) => Math.min(prev + 10, 200));
  };

  const handleZoomOut = () => {
    setZoom((prev) => Math.max(prev - 10, 50));
  };

  const handlePrevPage = () => {
    if (readingData?.hasPrevious) {
      setCurrentPage((prev) => Math.max(prev - 1, 1));
    }
  };

  const handleNextPage = () => {
    if (readingData?.hasNext) {
      setCurrentPage((prev) => prev + 1);
    }
  };

  const handleBack = () => {
    router.push("/dashboard");
  };

  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === "Escape" && settings.focusMode) {
        setSettings((prev) => ({ ...prev, focusMode: false }));
        return;
      }

      if ((e.ctrlKey || e.metaKey) && (e.key === "+" || e.key === "=")) {
        e.preventDefault();
        handleZoomIn();
        return;
      }

      if ((e.ctrlKey || e.metaKey) && e.key === "-") {
        e.preventDefault();
        handleZoomOut();
        return;
      }

      if (e.key === "ArrowLeft" || e.key === "PageUp") {
        e.preventDefault();
        handlePrevPage();
        return;
      }

      if (e.key === "ArrowRight" || e.key === "PageDown") {
        e.preventDefault();
        handleNextPage();
      }
    };

    window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);
  }, [settings.focusMode, readingData]);

  const getBackgroundColor = () => {
    if (settings.highContrast) return "#000000";
    if (settings.dyslexiaMode) return "#E8E3D3";
    return "oklch(14.5% 0 0)";
  };

  const getPageBackground = () => {
    if (settings.highContrast) return "#FFFFFF";
    if (settings.dyslexiaMode) return "#FFF8DC";
    return "#FFFFFF";
  };

  const getTextColor = () => {
    if (settings.highContrast) return "#000000";
    if (settings.dyslexiaMode) return "#1A1A1A";
    return "#1F2937";
  };

  const getFontFamily = () => {
    if (settings.dyslexiaMode) return "Comic Sans MS, Verdana, sans-serif";
    return "Inter, system-ui, sans-serif";
  };

  const getLetterSpacing = () => {
    if (settings.dyslexiaMode) return "0.12em";
    if (settings.highContrast) return "0.05em";
    return "normal";
  };

  const getWordSpacing = () => {
    if (settings.dyslexiaMode) return "0.16em";
    return "normal";
  };

  const getParagraphSpacing = () => {
    if (settings.dyslexiaMode) return "2em";
    return "1em";
  };

  const getFontWeight = (): 400 | 500 | 600 => {
    if (settings.dyslexiaMode) return 500;
    if (settings.highContrast) return 600;
    return 400;
  };

  const getTextAlign = (): "left" | "justify" => {
    if (settings.dyslexiaMode) return "left";
    return "justify";
  };

  const getHighlightBoxBackground = () => {
    if (settings.highContrast) return "#E5E5E5";
    if (settings.dyslexiaMode) return "#F5F0DC";
    return "#EFF6FF";
  };

  if (loading && !readingData) {
    return <div className="p-8">Carregando documento...</div>;
  }

  return (
    <div className="h-screen flex flex-col" style={{ backgroundColor: getBackgroundColor() }}>
      {!settings.focusMode && (
        <header className="bg-[#2D2D2D] border-b border-gray-700 h-[52px] px-4 flex items-center justify-between gap-6">
          <div className="flex items-center gap-3 flex-1 min-w-0">
            <Button
              variant="ghost"
              onClick={handleBack}
              className="text-white hover:bg-gray-700 h-8 px-2 rounded-md"
              aria-label="Voltar para dashboard"
            >
              <ArrowLeft className="w-5 h-5" />
            </Button>

            <button
              onClick={() => setShowSidebar(!showSidebar)}
              className="p-2 h-8 rounded-md hover:bg-gray-700 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
              aria-label={isSidebarVisible ? "Esconder páginas" : "Mostrar páginas"}
              title={isSidebarVisible ? "Esconder páginas" : "Mostrar páginas"}
            >
              {isSidebarVisible ? <X className="w-4 h-4" /> : <Menu className="w-4 h-4" />}
            </button>

            <h1 className="text-white text-sm font-normal truncate">{documentName}</h1>
          </div>

          <div className="flex items-center gap-0 bg-[#3F3F3F] rounded-md h-8">
            <Button
              variant="ghost"
              onClick={handleZoomOut}
              disabled={zoom <= 50}
              className="text-white hover:bg-gray-600 disabled:opacity-50 h-8 px-3 rounded-l-md rounded-r-none"
              aria-label="Diminuir zoom"
            >
              <ZoomOut className="w-4 h-4" />
            </Button>

            <span className="text-white text-sm px-3 min-w-[3.5rem] text-center bg-[#3F3F3F] h-8 flex items-center justify-center">
              {zoom}%
            </span>

            <Button
              variant="ghost"
              onClick={handleZoomIn}
              disabled={zoom >= 200}
              className="text-white hover:bg-gray-600 disabled:opacity-50 h-8 px-3 rounded-r-md rounded-l-none"
              aria-label="Aumentar zoom"
            >
              <ZoomIn className="w-4 h-4" />
            </Button>
          </div>

          <div className="flex items-center gap-3">
            <div className="flex items-center gap-0 bg-[#3F3F3F] rounded-md h-8">
              <Button
                variant="ghost"
                onClick={handlePrevPage}
                disabled={!readingData?.hasPrevious}
                className="text-white hover:bg-gray-600 disabled:opacity-50 h-8 px-2 rounded-l-md rounded-r-none"
                aria-label="Página anterior"
              >
                <ChevronLeft className="w-4 h-4" />
              </Button>

              <span className="text-white text-sm px-3 min-w-[4rem] text-center bg-[#3F3F3F] h-8 flex items-center justify-center">
                {readingData?.currentPage ?? currentPage} / {totalPages}
              </span>

              <Button
                variant="ghost"
                onClick={handleNextPage}
                disabled={!readingData?.hasNext}
                className="text-white hover:bg-gray-600 disabled:opacity-50 h-8 px-2 rounded-r-md rounded-l-none"
                aria-label="Próxima página"
              >
                <ChevronRight className="w-4 h-4" />
              </Button>
            </div>

            <div className="flex items-center gap-0 bg-[#3F3F3F] rounded-md h-8">
              <button
                onClick={() =>
                  setSettings((prev) => ({ ...prev, highContrast: !prev.highContrast }))
                }
                className={`h-8 px-3 rounded-l-md transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                  settings.highContrast
                    ? "bg-blue-600 text-white"
                    : "text-white hover:bg-gray-600"
                }`}
                aria-label={
                  settings.highContrast ? "Desativar alto contraste" : "Ativar alto contraste"
                }
                aria-pressed={settings.highContrast}
                title="Alto Contraste"
              >
                <Contrast className="w-4 h-4" />
              </button>

              <button
                onClick={() =>
                  setSettings((prev) => ({ ...prev, dyslexiaMode: !prev.dyslexiaMode }))
                }
                className={`h-8 px-3 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                  settings.dyslexiaMode
                    ? "bg-blue-600 text-white"
                    : "text-white hover:bg-gray-600"
                }`}
                aria-label={
                  settings.dyslexiaMode ? "Desativar modo dislexia" : "Ativar modo dislexia"
                }
                aria-pressed={settings.dyslexiaMode}
                title="Modo Dislexia"
              >
                <Brain className="w-4 h-4" />
              </button>

              <button
                onClick={() =>
                  setSettings((prev) => ({ ...prev, focusMode: !prev.focusMode }))
                }
                className={`h-8 px-3 rounded-r-md transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                  settings.focusMode
                    ? "bg-blue-600 text-white"
                    : "text-white hover:bg-gray-600"
                }`}
                aria-label={settings.focusMode ? "Desativar modo foco" : "Ativar modo foco"}
                aria-pressed={settings.focusMode}
                title="Modo Foco"
              >
                <Eye className="w-4 h-4" />
              </button>
            </div>

            <Button
              variant="ghost"
              onClick={() => setShowAccessibilityPanel(!showAccessibilityPanel)}
              className="text-white hover:bg-gray-700 h-8 px-2 rounded-md"
              aria-label="Configurações avançadas de acessibilidade"
              title="Configurações Avançadas"
            >
              <Settings className="w-5 h-5" />
            </Button>
          </div>
        </header>
      )}

      <div className="flex-1 flex overflow-hidden">
        {isSidebarVisible && (
          <aside className="w-60 bg-[#252525] border-r border-gray-700 overflow-y-auto">
            <div className="p-3 space-y-2">
              {previewPages.map((page) => (
                <button
                  key={page}
                  onClick={() => setCurrentPage(page)}
                  className={`w-full p-2 rounded text-white text-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                    (readingData?.currentPage ?? currentPage) === page
                      ? "bg-blue-600"
                      : "bg-gray-700 hover:bg-gray-600"
                  }`}
                  aria-label={`Ir para página ${page}`}
                  aria-current={(readingData?.currentPage ?? currentPage) === page ? "page" : undefined}
                >
                  <div className="aspect-[8.5/11] bg-white rounded mb-1 flex items-center justify-center text-gray-400 text-xs">
                    {page}
                  </div>
                  Página {page}
                </button>
              ))}
            </div>
          </aside>
        )}

        <main
          className="flex-1 overflow-auto"
          style={{
            padding: settings.focusMode ? "16px" : "32px",
          }}
        >
          {isFallbackMode && (
            <div className="max-w-[850px] mx-auto mb-4 rounded-xl border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-800 shadow-sm">
              Você está no modo demonstração. O conteúdo exibido é local porque o backend
              não respondeu com o documento real.
            </div>
          )}

          {!settings.focusMode && (settings.highContrast || settings.dyslexiaMode) && (
            <div className="max-w-[850px] mx-auto mb-4 flex gap-2 flex-wrap">
              {settings.highContrast && (
                <span className="inline-flex items-center gap-1.5 px-3 py-1 bg-blue-600 text-white text-xs rounded-full">
                  <Contrast className="w-3 h-3" />
                  Alto Contraste Ativo
                </span>
              )}

              {settings.dyslexiaMode && (
                <span className="inline-flex items-center gap-1.5 px-3 py-1 bg-purple-600 text-white text-xs rounded-full">
                  <Brain className="w-3 h-3" />
                  Modo Dislexia Ativo
                </span>
              )}
            </div>
          )}

          <div className="flex justify-center w-full overflow-x-auto">
            <div
              className="shadow-2xl rounded-sm"
              style={{
                backgroundColor: getPageBackground(),
                width: `${320+(settings.columnWidth-35)*16}px`,
                minHeight: "1100px",
                padding: settings.focusMode ? "32px" : "48px",
                transform: `scale(${zoom / 100})`,
                transformOrigin: "top center",
                marginBottom: zoom > 100 ? "200px" : "0",
                flexShrink: 0,
              }}
            >
              <article
                style={{
                  fontSize: `${settings.fontSize}px`,
                  lineHeight: settings.lineSpacing,
                  color: getTextColor(),
                  fontFamily: getFontFamily(),
                  letterSpacing: getLetterSpacing(),
                  wordSpacing: getWordSpacing(),
                  fontWeight: getFontWeight(),
                  textAlign: getTextAlign(),
                }}
              >
                <h1
                  style={{
                    fontSize: `${settings.fontSize * 1.875}px`,
                    marginBottom: getParagraphSpacing(),
                  }}
                >
                  {documentName}
                </h1>

                {readingData?.lines?.map((line, index) => (
                  <p key={index} style={{ marginBottom: getParagraphSpacing() }}>
                    {line}
                  </p>
                ))}

                <div
                  className="border-l-4 p-4 mt-6"
                  style={{
                    backgroundColor: getHighlightBoxBackground(),
                    borderLeftColor: settings.highContrast ? "#000000" : "#2563EB",
                    borderLeftWidth: "4px",
                    marginTop: `${parseFloat(getParagraphSpacing()) * 2}em`,
                  }}
                >
                  <p style={{ fontSize: `${settings.fontSize * 0.875}px` }}>
                    <strong style={{ fontWeight: settings.highContrast ? 700 : 600 }}>
                      Leitura adaptativa:
                    </strong>{" "}
                    o conteúdo principal já está sendo renderizado com as preferências de
                    leitura aplicadas, aproximando a experiência visual do leitor ao
                    comportamento esperado no projeto.
                  </p>
                </div>

                <div style={{ marginTop: `${parseFloat(getParagraphSpacing()) * 3}em` }}>
                  <h2
                    style={{
                      fontSize: `${settings.fontSize * 1.5}px`,
                      marginBottom: getParagraphSpacing(),
                    }}
                  >
                    Estado atual da experiência
                  </h2>

                  <div
                    className="border rounded p-4"
                    style={{
                      borderColor: settings.highContrast ? "#000000" : "#D1D5DB",
                      borderWidth: "2px",
                      marginBottom: getParagraphSpacing(),
                    }}
                  >
                    <p
                      style={{
                        fontSize: `${settings.fontSize * 0.875}px`,
                        marginBottom: "0.5em",
                      }}
                    >
                      <strong>Modos ativos agora:</strong>
                    </p>

                    <ul
                      className="ml-4"
                      style={{
                        listStyleType: "circle",
                        fontSize: `${settings.fontSize * 0.875}px`,
                      }}
                    >
                      {!settings.highContrast &&
                        !settings.dyslexiaMode &&
                        !settings.focusMode && <li>✓ Modo padrão</li>}

                      {settings.highContrast && (
                        <li>✓ Alto contraste com prioridade sobre as cores</li>
                      )}

                      {settings.dyslexiaMode && (
                        <li>✓ Modo dislexia com tipografia e espaçamentos ampliados</li>
                      )}

                      {settings.focusMode && <li>✓ Modo foco com interface reduzida</li>}
                    </ul>

                    {settings.highContrast && settings.dyslexiaMode && (
                      <p
                        style={{
                          fontSize: `${settings.fontSize * 0.875}px`,
                          marginTop: "1em",
                          padding: "8px",
                          backgroundColor: settings.highContrast ? "#CCCCCC" : "#DBEAFE",
                        }}
                      >
                        <strong>Combinação ativa:</strong> Alto contraste controla as cores,
                        enquanto o modo dislexia controla tipografia, alinhamento e
                        espaçamentos.
                      </p>
                    )}
                  </div>
                </div>
              </article>
            </div>
          </div>
        </main>

        <AccessibilityPanel
          isOpen={showAccessibilityPanel}
          onClose={() => setShowAccessibilityPanel(false)}
          settings={settings}
          onSettingsChange={setSettings}
        />
      </div>

      {settings.focusMode && (
        <div className="fixed bottom-4 right-4 bg-gray-900 text-white px-4 py-2 rounded-lg shadow-lg text-sm">
          Pressione <kbd className="px-2 py-1 bg-gray-700 rounded">Esc</kbd> para sair do
          modo foco
        </div>
      )}
    </div>
  );
}