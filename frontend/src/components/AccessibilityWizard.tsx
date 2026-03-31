"use client";

import React, { useState } from 'react';
import { Settings, ArrowRight, CheckCircle, Brain, Eye, Type } from 'lucide-react';
import { Button } from './Button';
import { Card } from './Card';
// Certifique-se de que o arquivo de tipos exista em: src/app/types/accessibility.ts
import { AccessibilitySettings, WizardAnswers } from '@/app/types/accessibility';

interface AccessibilityWizardProps {
  isOpen: boolean;
  onClose: () => void;
  onApply: (settings: AccessibilitySettings) => void;
  currentSettings: AccessibilitySettings;
}

export function AccessibilityWizard({ isOpen, onClose, onApply, currentSettings }: AccessibilityWizardProps) {
  const [step, setStep] = useState(0);
  const [answers, setAnswers] = useState<WizardAnswers>({
    smallText: null,
    distraction: null,
    spacing: null,
  });

  if (!isOpen) return null;

  const handleAnswer = (key: keyof WizardAnswers, value: boolean) => {
    setAnswers(prev => ({ ...prev, [key]: value }));
    setStep(prev => prev + 1);
  };

  const handleFinish = () => {
    const newSettings = { ...currentSettings };
    
    // Lógica de recomendação baseada nas respostas
    if (answers.smallText) {
      newSettings.fontSize = 22;
      newSettings.lineSpacing = 1.8;
    }
    if (answers.distraction) {
      newSettings.focusMode = true;
    }
    if (answers.spacing) {
      newSettings.dyslexiaMode = true;
      newSettings.lineSpacing = 2.0;
    }
    
    onApply(newSettings);
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black/60 flex items-center justify-center z-50 p-4 backdrop-blur-sm">
      <Card className="w-full max-w-lg p-8 bg-white shadow-2xl">
        
        {/* PASSO 0: INTRODUÇÃO */}
        {step === 0 && (
          <div className="text-center space-y-4">
            <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto">
              <Settings className="w-8 h-8 text-blue-600" />
            </div>
            <h2 className="text-2xl font-bold text-gray-900">Personalizar Leitura</h2>
            <p className="text-gray-600">Responda 3 perguntas rápidas para configurarmos o leitor para você.</p>
            <div className="flex gap-3 pt-4">
              <Button variant="ghost" className="flex-1" onClick={onClose}>Pular</Button>
              <Button className="flex-1" onClick={() => setStep(1)}>
                Começar <ArrowRight className="ml-2 w-4 h-4"/>
              </Button>
            </div>
          </div>
        )}

        {/* PASSO 1: TAMANHO DO TEXTO */}
        {step === 1 && (
          <div className="space-y-6">
            <div className="flex items-center gap-2 text-blue-600 font-medium">
              <Type className="w-5 h-5"/> <span>Pergunta 1 de 3</span>
            </div>
            <h3 className="text-xl font-semibold text-gray-900">Você tem dificuldade para ler textos pequenos?</h3>
            <div className="grid grid-cols-2 gap-4">
              <Button variant="secondary" onClick={() => handleAnswer('smallText', true)}>Sim, prefiro maior</Button>
              <Button variant="secondary" onClick={() => handleAnswer('smallText', false)}>Não, está bom</Button>
            </div>
          </div>
        )}

        {/* PASSO 2: DISTRAÇÃO */}
        {step === 2 && (
          <div className="space-y-6">
            <div className="flex items-center gap-2 text-blue-600 font-medium">
              <Eye className="w-5 h-5"/> <span>Pergunta 2 de 3</span>
            </div>
            <h3 className="text-xl font-semibold text-gray-900">Você se distrai facilmente com menus e ícones na tela?</h3>
            <div className="grid grid-cols-2 gap-4">
              <Button variant="secondary" onClick={() => handleAnswer('distraction', true)}>Sim, me distraio</Button>
              <Button variant="secondary" onClick={() => handleAnswer('distraction', false)}>Não, não incomoda</Button>
            </div>
          </div>
        )}

        {/* PASSO 3: ESPAÇAMENTO / DISLEXIA */}
        {step === 3 && (
          <div className="space-y-6">
            <div className="flex items-center gap-2 text-blue-600 font-medium">
              <Brain className="w-5 h-5"/> <span>Pergunta 3 de 3</span>
            </div>
            <h3 className="text-xl font-semibold text-gray-900">Prefere mais espaço entre as linhas e letras para ler melhor?</h3>
            <div className="grid grid-cols-2 gap-4">
              <Button variant="secondary" onClick={() => handleAnswer('spacing', true)}>Sim, por favor</Button>
              <Button variant="secondary" onClick={() => handleAnswer('spacing', false)}>Não, padrão</Button>
            </div>
          </div>
        )}

        {/* PASSO FINAL: SUCESSO */}
        {step === 4 && (
          <div className="text-center space-y-4">
            <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto">
              <CheckCircle className="w-8 h-8 text-green-600" />
            </div>
            <h2 className="text-2xl font-bold text-gray-900">Tudo pronto!</h2>
            <p className="text-gray-600">Calculamos as melhores configurações baseadas no seu perfil.</p>
            <Button onClick={handleFinish} className="w-full mt-4">
              Aplicar Configurações
            </Button>
          </div>
        )}
      </Card>
    </div>
  );
}