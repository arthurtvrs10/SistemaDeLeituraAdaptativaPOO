"use client";

import {
  X,
  Type,
  AlignLeft,
  Columns,
  Contrast,
  Eye,
  Brain,
  Save,
} from "lucide-react";
import * as Slider from "@radix-ui/react-slider";
import * as Switch from "@radix-ui/react-switch";
import { useAuth } from "@/context/AuthContext";
import { Button } from "@/components/Button";
import api from "@/services/api";

interface AccessibilitySettings {
  fontSize: number;
  lineSpacing: number;
  columnWidth: number;
  highContrast: boolean;
  focusMode: boolean;
  dyslexiaMode: boolean;
}

interface AccessibilityPanelProps {
  isOpen: boolean;
  onClose: () => void;
  settings: AccessibilitySettings;
  onSettingsChange: (settings: AccessibilitySettings) => void;
}

export function AccessibilityPanel({
  isOpen,
  onClose,
  settings,
  onSettingsChange,
}: AccessibilityPanelProps) {
  const { isLoggedIn, setShowLoginPrompt } = useAuth();

  if (!isOpen) return null;

  const applyBusinessRules = (
    next: AccessibilitySettings,
    changedKey?: keyof AccessibilitySettings
  ): AccessibilitySettings => {
    const updated = { ...next };

    /**
     * REGRA 1:
     * Alto contraste controla CORES.
     * Não força mudanças tipográficas.
     */

    /**
     * REGRA 2:
     * Dislexia controla TIPOGRAFIA e espaçamentos.
     * No seu reader atual:
     * - fonte acessível
     * - letterSpacing 0.12em
     * - wordSpacing 0.16em
     * - textAlign left
     * - fontWeight 500
     * - fundo bege só se alto contraste estiver desligado
     *
     * Aqui no painel, o que conseguimos refletir diretamente no estado é:
     * - lineSpacing mais confortável ao ativar dislexia
     *
     * Sem destruir ajustes manuais do usuário toda vez, a regra só força
     * lineSpacing mínima quando a mudança foi ativar dyslexiaMode.
     */
    if (changedKey === "dyslexiaMode" && updated.dyslexiaMode) {
      if (updated.lineSpacing < 1.8) {
        updated.lineSpacing = 1.8;
      }
    }

    /**
     * REGRA 3:
     * Modo foco é independente e não altera as demais configurações.
     */

    /**
     * REGRA 4:
     * Garantir limites coerentes.
     */
    updated.fontSize = Math.min(28, Math.max(12, updated.fontSize));
    updated.lineSpacing = Math.min(2.5, Math.max(1.0, updated.lineSpacing));
    updated.columnWidth = Math.min(100, Math.max(50, updated.columnWidth));

    return updated;
  };

  const updateSetting = <K extends keyof AccessibilitySettings>(
    key: K,
    value: AccessibilitySettings[K]
  ) => {
    const next = applyBusinessRules(
      { ...settings, [key]: value },
      key
    );

    onSettingsChange(next);
    localStorage.setItem("accessibility-settings", JSON.stringify(next));
  };

  const handleSavePreferences = async () => {
    if (!isLoggedIn) {
      setShowLoginPrompt(true);
      return;
    }

    try {
      const payload = {
        fontSize: settings.fontSize,
        lineHeight: settings.lineSpacing,
        columnWidth: settings.columnWidth,
        theme: settings.highContrast ? "HIGH_CONTRAST" : "LIGHT",
        focusMode: settings.focusMode,
        dyslexiaFriendlyFont: settings.dyslexiaMode,
        colorBlindMode: false,
        keyboardPreferred: true,
      };

      await api.post("/accessibility/profile", payload);
      localStorage.setItem("accessibility-settings", JSON.stringify(settings));
      alert("Preferências salvas com sucesso!");
    } catch (error) {
      console.error("Erro ao salvar preferências:", error);
      localStorage.setItem("accessibility-settings", JSON.stringify(settings));
      alert("Não foi possível salvar no servidor. As preferências foram salvas localmente.");
    }
  };

  return (
    <div
      className="fixed top-0 right-0 h-full w-80 bg-white shadow-2xl border-l border-gray-200 z-50 overflow-y-auto"
      role="dialog"
      aria-label="Painel de Acessibilidade"
    >
      <div className="sticky top-0 bg-white border-b border-gray-200 p-4 flex items-center justify-between">
        <h2 className="text-lg text-gray-900">Acessibilidade</h2>
        <button
          onClick={onClose}
          className="w-8 h-8 flex items-center text-gray-900 justify-center rounded-lg hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-500 "
          aria-label="Fechar painel"
        >
          <X className="w-5 h-5" />
        </button>
      </div>

      <div className="p-4 space-y-6">
        <div>
          <label className="flex items-center gap-2 mb-3 text-gray-900">
            <Type className="w-5 h-5" />
            <span>Tamanho da Fonte</span>
          </label>
          <Slider.Root
            className="relative flex items-center select-none touch-none w-full h-5"
            value={[settings.fontSize]}
            onValueChange={([value]) => updateSetting("fontSize", value)}
            min={12}
            max={28}
            step={1}
            aria-label="Tamanho da fonte"
          >
            <Slider.Track className="bg-gray-200 relative grow rounded-full h-1">
              <Slider.Range className="absolute bg-blue-600 rounded-full h-full" />
            </Slider.Track>
            <Slider.Thumb className="block w-5 h-5 bg-white border-2 border-blue-600 rounded-full hover:bg-blue-50 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </Slider.Root>
          <div className="flex justify-between text-sm text-gray-600 mt-1">
            <span>12px</span>
            <span className="font-medium">{settings.fontSize}px</span>
            <span>28px</span>
          </div>
        </div>

        <div>
          <label className="flex items-center gap-2 mb-3 text-gray-900">
            <AlignLeft className="w-5 h-5" />
            <span>Espaçamento de Linhas</span>
          </label>
          <Slider.Root
            className="relative flex items-center select-none touch-none w-full h-5"
            value={[settings.lineSpacing]}
            onValueChange={([value]) => updateSetting("lineSpacing", value)}
            min={1.0}
            max={2.5}
            step={0.1}
            aria-label="Espaçamento de linhas"
          >
            <Slider.Track className="bg-gray-200 relative grow rounded-full h-1">
              <Slider.Range className="absolute bg-blue-600 rounded-full h-full" />
            </Slider.Track>
            <Slider.Thumb className="block w-5 h-5 bg-white border-2 border-blue-600 rounded-full hover:bg-blue-50 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </Slider.Root>
          <div className="flex justify-between text-sm text-gray-600 mt-1">
            <span>1.0</span>
            <span className="font-medium">{settings.lineSpacing.toFixed(1)}</span>
            <span>2.5</span>
          </div>
        </div>

        <div>
          <label className="flex items-center gap-2 mb-3 text-gray-900">
            <Columns className="w-5 h-5" />
            <span>Largura da Coluna</span>
          </label>
          <Slider.Root
            className="relative flex items-center select-none touch-none w-full h-5"
            value={[settings.columnWidth]}
            onValueChange={([value]) => updateSetting("columnWidth", value)}
            min={35}
            max={95}
            step={5}
            aria-label="Largura da coluna"
          >
            <Slider.Track className="bg-gray-200 relative grow rounded-full h-1">
              <Slider.Range className="absolute bg-blue-600 rounded-full h-full" />
            </Slider.Track>
            <Slider.Thumb className="block w-5 h-5 bg-white border-2 border-blue-600 rounded-full hover:bg-blue-50 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </Slider.Root>
          <div className="flex justify-between text-sm text-gray-600 mt-1">
            <span>35%</span>
            <span className="font-medium">{settings.columnWidth}%</span>
            <span>95%</span>
          </div>
        </div>

        <div className="border-t border-gray-200 pt-6 space-y-4">
          <div className="flex items-start justify-between gap-3">
            <div className="flex-1">
              <label
                htmlFor="high-contrast"
                className="flex items-center gap-2 text-gray-900 mb-1 cursor-pointer"
              >
                <Contrast className="w-5 h-5" />
                <span>Alto Contraste</span>
              </label>
              <p className="text-xs text-gray-600">
                Controla as cores do leitor com contraste máximo entre texto e fundo.
                No seu reader atual, o alto contraste tem prioridade visual sobre as cores.
              </p>
            </div>
            <Switch.Root
              id="high-contrast"
              checked={settings.highContrast}
              onCheckedChange={(checked) => updateSetting("highContrast", checked)}
              className="w-11 h-6 bg-gray-200 rounded-full relative data-[state=checked]:bg-blue-600 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 flex-shrink-0"
            >
              <Switch.Thumb className="block w-5 h-5 bg-white rounded-full transition-transform translate-x-0.5 data-[state=checked]:translate-x-[22px]" />
            </Switch.Root>
          </div>

          <div className="flex items-start justify-between gap-3">
            <div className="flex-1">
              <label
                htmlFor="focus-mode"
                className="flex items-center gap-2 text-gray-900 mb-1 cursor-pointer"
              >
                <Eye className="w-5 h-5" />
                <span>Modo Foco</span>
              </label>
              <p className="text-xs text-gray-600">
                Remove distrações e simplifica a interface. No reader atual, ele
                atua de forma independente dos outros modos.
              </p>
            </div>
            <Switch.Root
              id="focus-mode"
              checked={settings.focusMode}
              onCheckedChange={(checked) => updateSetting("focusMode", checked)}
              className="w-11 h-6 bg-gray-200 rounded-full relative data-[state=checked]:bg-blue-600 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 flex-shrink-0"
            >
              <Switch.Thumb className="block w-5 h-5 bg-white rounded-full transition-transform translate-x-0.5 data-[state=checked]:translate-x-[22px]" />
            </Switch.Root>
          </div>

          <div className="flex items-start justify-between gap-3">
            <div className="flex-1">
              <label
                htmlFor="dyslexia-mode"
                className="flex items-center gap-2 text-gray-900 mb-1 cursor-pointer"
              >
                <Brain className="w-5 h-5" />
                <span>Modo Dislexia</span>
              </label>
              <p className="text-xs text-gray-600">
                Prioriza tipografia e espaçamentos. No seu reader atual, esse modo
                controla fonte acessível, espaçamento ampliado e alinhamento à esquerda.
              </p>
            </div>
            <Switch.Root
              id="dyslexia-mode"
              checked={settings.dyslexiaMode}
              onCheckedChange={(checked) => updateSetting("dyslexiaMode", checked)}
              className="w-11 h-6 bg-gray-200 rounded-full relative data-[state=checked]:bg-blue-600 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 flex-shrink-0"
            >
              <Switch.Thumb className="block w-5 h-5 bg-white rounded-full transition-transform translate-x-0.5 data-[state=checked]:translate-x-[22px]" />
            </Switch.Root>
          </div>
        </div>

        <div className="bg-blue-50 rounded-lg p-4 border border-blue-200">
          <p className="text-sm text-blue-900">
            <strong>Atalhos de teclado:</strong>
            <br />•{" "}
            <kbd className="px-1.5 py-0.5 bg-white rounded border border-blue-300 text-xs">
              Ctrl + +
            </kbd>{" "}
            Aumentar zoom
            <br />•{" "}
            <kbd className="px-1.5 py-0.5 bg-white rounded border border-blue-300 text-xs">
              Ctrl + -
            </kbd>{" "}
            Diminuir zoom
            <br />•{" "}
            <kbd className="px-1.5 py-0.5 bg-white rounded border border-blue-300 text-xs">
              Esc
            </kbd>{" "}
            Sair do modo foco
          </p>
        </div>

        {(settings.highContrast || settings.focusMode || settings.dyslexiaMode) && (
          <div className="bg-green-50 rounded-lg p-4 border border-green-200">
            <p className="text-sm text-green-900 mb-2">
              <strong>✓ Modo(s) ativo(s):</strong>
            </p>
            <ul className="text-xs text-green-800 space-y-1">
              {settings.highContrast && <li>• Alto Contraste</li>}
              {settings.focusMode && <li>• Modo Foco</li>}
              {settings.dyslexiaMode && <li>• Modo Dislexia</li>}
            </ul>
          </div>
        )}

        {settings.highContrast && settings.dyslexiaMode && (
          <div className="bg-blue-50 rounded-lg p-4 border border-blue-200">
            <p className="text-xs text-blue-900">
              <strong>💡 Combinação Ativa:</strong>
              <br />
              Alto Contraste + Dislexia trabalham juntos:
              <br />• <strong>Cores:</strong> controladas pelo alto contraste
              <br />• <strong>Tipografia:</strong> controlada pelo modo dislexia
              <br />• <strong>Alinhamento:</strong> texto à esquerda no conteúdo
            </p>
          </div>
        )}

        {isLoggedIn ? (
          <div className="mt-6">
            <Button onClick={handleSavePreferences} className="w-full">
              <Save className="w-5 h-5 mr-2" />
              Salvar Configurações
            </Button>
          </div>
        ) : (
          <div className="mt-6 bg-amber-50 border border-amber-200 rounded-lg p-4">
            <p className="text-sm text-amber-900 mb-3">
              <strong>💡 Salve suas preferências</strong>
              <br />
              Faça login para salvar suas configurações de acessibilidade e progresso
              de leitura.
            </p>
            <Button
              onClick={handleSavePreferences}
              variant="secondary"
              className="w-full"
            >
              Fazer Login para Salvar
            </Button>
          </div>
        )}
      </div>
    </div>
  );
}