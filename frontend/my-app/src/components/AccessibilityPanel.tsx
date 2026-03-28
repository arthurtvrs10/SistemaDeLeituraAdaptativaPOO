"use client";

import { X, Type, AlignLeft, Columns, Contrast, Eye, Brain, Save } from 'lucide-react';
import * as Slider from '@radix-ui/react-slider';
import * as Switch from '@radix-ui/react-switch';
import { useAuth } from '@/context/AuthContext'; // Ajuste o path conforme seu projeto
import { Button } from './Button';

// ... (Interfaces AccessibilitySettings e AccessibilityPanelProps mantidas iguais)

export function AccessibilityPanel({ isOpen, onClose, settings, onSettingsChange }: AccessibilityPanelProps) {
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
      console.log('Settings saved:', settings);
      alert('Preferências salvas com sucesso!');
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
      {/* ... (O restante do JSX permanece idêntico ao seu original) ... */}
    </div>
  );
}