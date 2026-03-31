"use client";

import React, { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import {
  ArrowLeft,
  ZoomIn,
  ZoomOut,
  ChevronLeft,
  ChevronRight,
  Settings,
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
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
};

const FALLBACK_TOTAL_PAGES = 3;

const getFallbackLines = (page: number): string[] => {
  const pages: Record<number, string[]> = {
    1: [
      "Bem-vindo ao Sistema de Leitura Adaptativa.",
      "Este é um conteúdo de demonstração exibido quando o backend não está disponível.",
      "O objetivo é garantir uma experiência visual agradável, estável e acessível mesmo em cenários de falha.",
      "Mesmo sem conexão com a API, o usuário pode testar zoom, contraste, modo dislexia e modo foco.",
      "Essa abordagem evita a sensação de erro ou abandono da interface."
    ],
    2: [
      "No modo demonstração, o leitor continua funcionando para apresentação do projeto.",
      "Isso é útil em reuniões, testes locais, avaliações acadêmicas e validações de interface.",
      "O layout continua legível, confortável e consistente com a proposta do sistema.",
      "Quando o backend estiver online, o conteúdo real será carregado automaticamente.",
      "As preferências do usuário também poderão ser recuperadas da API."
    ],
    3: [
      "O Sistema de Leitura Adaptativa foi pensado para personalizar a experiência de leitura.",
      "Entre os recursos estão ajuste de fonte, espaçamento, largura de coluna, contraste e foco.",
      "O modo dislexia prioriza tipografia e espaçamentos mais confortáveis para leitura prolongada.",
      "O alto contraste prioriza cores fortes e maior distinção visual.",
      "Assim, o leitor permanece útil e acolhedor, mesmo em ambiente offline."
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
  const [showAccessibilityPanel, setShowAccessibilityPanel] = useState(false);
  const [isFallbackMode, setIsFallbackMode] = useState(false);

  const [settings, setSettings] = useState<AccessibilitySettings>({
    fontSize: 18,
    lineSpacing: 1.6,
    columnWidth: 78,
    highContrast: false,
    focusMode: false,
    dyslexiaMode: false,
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

        setSettings({
          fontSize: data.fontSize ?? 18,
          lineSpacing: data.lineHeight ?? 1.6,
          columnWidth: data.columnWidth ?? 78,
          highContrast: data.theme === "HIGH_CONTRAST",
          focusMode: data.focusMode ?? false,
          dyslexiaMode: data.dyslexiaFriendlyFont ?? false,
        });
      } catch (err) {
        console.error("Erro ao carregar perfil de acessibilidade:", err);

        setSettings({
          fontSize: 18,
          lineSpacing: 1.6,
          columnWidth: 78,
          highContrast: false,
          focusMode: false,
          dyslexiaMode: false,
        });
      }
    }

    loadProfile();
  }, []);

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

  const getBackgroundColor = () => {
    if (settings.highContrast) return "#000000";
    if (settings.dyslexiaMode) return "#E8E3D3";
    return "#EEF2F7";
  };

  const getPageBackground = () => {
    if (settings.highContrast) return "#FFFFFF";
    if (settings.dyslexiaMode) return "#FFF8DC";
    return "#FFFEFB";
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

  if (loading && !readingData) {
    return <div className="p-8">Carregando documento...</div>;
  }

  return (
    <div
      className="min-h-screen flex flex-col"
      style={{ backgroundColor: getBackgroundColor() }}
    >
      {!settings.focusMode && (
        <header className="bg-[#2D2D2D] text-white p-3 flex justify-between items-center gap-4">
          <div className="flex items-center gap-4 min-w-0">
            <Button
              variant="ghost"
              onClick={() => router.push("/dashboard")}
              className="text-white"
            >
              <ArrowLeft className="w-5 h-5" />
            </Button>

            <span className="truncate">
              {readingData?.documentTitle || `Documento ${id}`}
            </span>
          </div>

          <div className="flex items-center gap-2">
            <Button variant="ghost" onClick={handleZoomOut} className="text-white">
              <ZoomOut className="w-5 h-5" />
            </Button>

            <span className="text-sm min-w-[60px] text-center">{zoom}%</span>

            <Button variant="ghost" onClick={handleZoomIn} className="text-white">
              <ZoomIn className="w-5 h-5" />
            </Button>
          </div>

          <div className="flex items-center gap-2">
            <button
              onClick={() =>
                setSettings((prev) => ({
                  ...prev,
                  highContrast: !prev.highContrast,
                }))
              }
              className={`px-3 py-2 rounded transition-colors ${
                settings.highContrast ? "bg-blue-600 text-white" : "bg-gray-700 text-white"
              }`}
              aria-label="Alternar alto contraste"
              aria-pressed={settings.highContrast}
              title="Alto contraste"
            >
              <Contrast className="w-4 h-4" />
            </button>

            <button
              onClick={() =>
                setSettings((prev) => ({
                  ...prev,
                  dyslexiaMode: !prev.dyslexiaMode,
                }))
              }
              className={`px-3 py-2 rounded transition-colors ${
                settings.dyslexiaMode ? "bg-blue-600 text-white" : "bg-gray-700 text-white"
              }`}
              aria-label="Alternar modo dislexia"
              aria-pressed={settings.dyslexiaMode}
              title="Modo dislexia"
            >
              <Brain className="w-4 h-4" />
            </button>

            <button
              onClick={() =>
                setSettings((prev) => ({
                  ...prev,
                  focusMode: !prev.focusMode,
                }))
              }
              className={`px-3 py-2 rounded transition-colors ${
                settings.focusMode ? "bg-blue-600 text-white" : "bg-gray-700 text-white"
              }`}
              aria-label="Alternar modo foco"
              aria-pressed={settings.focusMode}
              title="Modo foco"
            >
              <Eye className="w-4 h-4" />
            </button>

            <Button
              variant="ghost"
              onClick={() => setShowAccessibilityPanel(true)}
              className="text-white"
            >
              <Settings className="w-5 h-5" />
            </Button>
          </div>
        </header>
      )}

      <main className="flex-1 overflow-auto p-8">
        {isFallbackMode && (
          <div className="max-w-4xl mx-auto mb-4 rounded-xl border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-800 shadow-sm">
            Você está no modo demonstração. O conteúdo exibido é local porque o backend
            está indisponível no momento.
          </div>
        )}

        <div className="flex justify-center">
          <div
            className="shadow-lg"
            style={{
              backgroundColor: getPageBackground(),
              width: `${settings.columnWidth}%`,
              maxWidth: settings.focusMode ? "100%" : "900px",
              minHeight: "1000px",
              padding: settings.focusMode ? "32px" : "48px",
              transform: `scale(${zoom / 100})`,
              transformOrigin: "top center",
              marginBottom: zoom > 100 ? "180px" : "0",
              borderRadius: "8px",
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
                  fontSize: `${settings.fontSize * 1.8}px`,
                  marginBottom: getParagraphSpacing(),
                }}
              >
                {readingData?.documentTitle || `Documento ${id}`}
              </h1>

              {readingData?.lines?.map((line, index) => (
                <p key={index} style={{ marginBottom: getParagraphSpacing() }}>
                  {line}
                </p>
              ))}
            </article>
          </div>
        </div>

        <div className="flex justify-center mt-8">
          <div className="flex items-center gap-4 bg-white shadow px-4 py-3 rounded">
            <Button
              variant="ghost"
              onClick={handlePrevPage}
              disabled={!readingData?.hasPrevious}
            >
              <ChevronLeft className="w-5 h-5" />
            </Button>

            <span>
              Página {readingData?.currentPage ?? currentPage} /{" "}
              {readingData?.totalPages ?? FALLBACK_TOTAL_PAGES}
            </span>

            <Button
              variant="ghost"
              onClick={handleNextPage}
              disabled={!readingData?.hasNext}
            >
              <ChevronRight className="w-5 h-5" />
            </Button>
          </div>
        </div>
      </main>

      <AccessibilityPanel
        isOpen={showAccessibilityPanel}
        onClose={() => setShowAccessibilityPanel(false)}
        settings={settings}
        onSettingsChange={setSettings}
      />

      {settings.focusMode && (
        <div className="fixed bottom-4 right-4 bg-gray-900 text-white px-4 py-2 rounded-lg shadow-lg text-sm">
          Pressione <kbd className="px-2 py-1 bg-gray-700 rounded">Esc</kbd> para sair
          do modo foco
        </div>
      )}
    </div>
  );
}