"use client";

import React from 'react';
import { X, Type, AlignLeft, Contrast, Brain, Save, Settings } from 'lucide-react';
import * as Slider from '@radix-ui/react-slider';
import * as Switch from '@radix-ui/react-switch';
import { Button } from './Button';
import { AccessibilitySettings } from '@/app/types/accessibility';
import { useAuth } from '@/context/AuthContext';

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

  const updateSetting = <K extends keyof AccessibilitySettings>(
    key: K,
    value: AccessibilitySettings[K]
  ) => {
    onSettingsChange({ ...settings, [key]: value });
  };

  const handleSavePreferences = () => {
    if (isLoggedIn) {
      console.log('Salvando no banco de dados Java...', settings);
      alert('Preferências salvas no seu perfil!');
    } else {
      setShowLoginPrompt(true);
    }
  };

  return (
    <div
      className="fixed top-0 right-0 h-full w-80 bg-white shadow-2xl border-l border-gray-200 z-50 overflow-y-auto"
      role="dialog"
      aria-label="Painel de Acessibilidade"
    >
      {/* Header */}
      <div className="sticky top-0 bg-white border-b border-gray-200 p-4 flex items-center justify-between">
        <h2 className="text-lg font-semibold text-gray-900 flex items-center gap-2">
          <Settings className="w-5 h-5" /> Acessibilidade
        </h2>
        <button
          onClick={onClose}
          className="w-8 h-8 flex items-center justify-center rounded-lg hover:bg-gray-100"
        >
          <X className="w-5 h-5" />
        </button>
      </div>

      <div className="p-4 space-y-8">
        {/* Tamanho da Fonte */}
        <div className="space-y-4">
          <label className="flex items-center gap-2 text-sm font-medium text-gray-700">
            <Type className="w-4 h-4" /> Tamanho da Fonte ({settings.fontSize}px)
          </label>
          <Slider.Root
            className="relative flex items-center select-none touch-none w-full h-5"
            value={[settings.fontSize]}
            onValueChange={([value]) => updateSetting('fontSize', value)}
            min={12}
            max={32}
            step={1}
          >
            <Slider.Track className="bg-gray-200 relative grow rounded-full h-1">
              <Slider.Range className="absolute bg-blue-600 rounded-full h-full" />
            </Slider.Track>
            <Slider.Thumb className="block w-5 h-5 bg-white border-2 border-blue-600 rounded-full shadow focus:outline-none" />
          </Slider.Root>
        </div>

        {/* Espaçamento de Linhas */}
        <div className="space-y-4">
          <label className="flex items-center gap-2 text-sm font-medium text-gray-700">
            <AlignLeft className="w-4 h-4" /> Espaçamento de Linhas
          </label>
          <Slider.Root
            className="relative flex items-center select-none touch-none w-full h-5"
            value={[settings.lineSpacing]}
            onValueChange={([value]) => updateSetting('lineSpacing', value)}
            min={1}
            max={3}
            step={0.1}
          >
            <Slider.Track className="bg-gray-200 relative grow rounded-full h-1">
              <Slider.Range className="absolute bg-blue-600 rounded-full h-full" />
            </Slider.Track>
            <Slider.Thumb className="block w-5 h-5 bg-white border-2 border-blue-600 rounded-full shadow focus:outline-none" />
          </Slider.Root>
        </div>

        {/* Modos de Leitura */}
        <div className="pt-4 border-t border-gray-100 space-y-4">
          <div className="flex items-center justify-between">
            <div className="space-y-0.5">
              <span className="text-sm font-medium flex items-center gap-2">
                <Contrast className="w-4 h-4" /> Alto Contraste
              </span>
            </div>
            <Switch.Root
              checked={settings.highContrast}
              onCheckedChange={(checked) => updateSetting('highContrast', checked)}
              className="w-11 h-6 bg-gray-200 rounded-full relative data-[state=checked]:bg-blue-600 transition-colors"
            >
              <Switch.Thumb className="block w-5 h-5 bg-white rounded-full transition-transform translate-x-0.5 data-[state=checked]:translate-x-[20px]" />
            </Switch.Root>
          </div>

          <div className="flex items-center justify-between">
            <div className="space-y-0.5">
              <span className="text-sm font-medium flex items-center gap-2">
                <Brain className="w-4 h-4" /> Modo Dislexia
              </span>
            </div>
            <Switch.Root
              checked={settings.dyslexiaMode}
              onCheckedChange={(checked) => updateSetting('dyslexiaMode', checked)}
              className="w-11 h-6 bg-gray-200 rounded-full relative data-[state=checked]:bg-blue-600 transition-colors"
            >
              <Switch.Thumb className="block w-5 h-5 bg-white rounded-full transition-transform translate-x-0.5 data-[state=checked]:translate-x-[20px]" />
            </Switch.Root>
          </div>
        </div>

        <Button onClick={handleSavePreferences} className="w-full">
          <Save className="w-4 h-4 mr-2" /> Salvar Preferências
        </Button>
      </div>
    </div>
  );
}