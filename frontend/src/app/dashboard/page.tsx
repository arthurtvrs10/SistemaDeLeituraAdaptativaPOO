"use client";

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Button } from '@/components/Button';
import { FileText, Upload, LogOut, Search, UserCircle, Wand2 } from 'lucide-react';
import { useAuth } from '@/context/AuthContext';
import { AccessibilityWizard } from '@/components/AccessibilityWizard';

interface Document {
  id: string;
  name: string;
  uploadDate: string;
  pages: number;
}

// Interface para as configurações de acessibilidade
interface AccessibilitySettings {
  fontSize: number;
  lineSpacing: number;
  columnWidth: number;
  highContrast: boolean;
  focusMode: boolean;
  dyslexiaMode: boolean;
}

export default function Dashboard() {
  const router = useRouter();
  const { isLoggedIn, userEmail, logout } = useAuth();
  const [searchQuery, setSearchQuery] = useState('');
  const [showWizard, setShowWizard] = useState(false);
  
  // Corrigido: Inicializando direto do localStorage para evitar cascading renders no useEffect
  const [wizardCompleted, setWizardCompleted] = useState<boolean>(false);

  useEffect(() => {
    const completed = localStorage.getItem('accessibility-wizard-completed') === 'true';
    // eslint-disable-next-line react-hooks/set-state-in-effect
    setWizardCompleted(completed);
  }, []);

  // Mock de documentos (Posteriormente substituir por chamada à API)
  const [documents] = useState<Document[]>([
    { id: '1', name: 'Introdução à Acessibilidade Web.pdf', uploadDate: '2026-03-20', pages: 45 },
    { id: '2', name: 'WCAG 2.2 Diretrizes Completas.pdf', uploadDate: '2026-03-18', pages: 120 },
    { id: '3', name: 'Design Inclusivo - Guia Prático.pdf', uploadDate: '2026-03-15', pages: 78 },
    { id: '4', name: 'Tecnologias Assistivas.pdf', uploadDate: '2026-03-10', pages: 56 },
  ]);

  const filteredDocuments = documents.filter(doc =>
    doc.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleUpload = () => {
    alert('Recurso de upload disponível em breve (Integração com Backend Java)');
  };

  const handleLogout = () => {
    logout();
    router.push('/');
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 bg-blue-600 rounded-md flex items-center justify-center">
              <FileText className="w-5 h-5 text-white" />
            </div>
            <div>
              <div className="flex items-center gap-2">
                <h1 className="text-lg font-semibold text-gray-900">Sistema de Leitura</h1>
                {!isLoggedIn && (
                  <span className="inline-flex items-center gap-1 px-2 py-0.5 bg-amber-50 border border-amber-200 rounded text-xs text-amber-700 font-medium">
                    <UserCircle className="w-3 h-3" />
                    Modo visitante
                  </span>
                )}
              </div>
              <p className="text-sm text-gray-600">
                {isLoggedIn ? userEmail : 'Seus documentos offline'}
              </p>
            </div>
          </div>
          <div className="flex items-center gap-2">
            {!isLoggedIn && (
              <Button variant="secondary" onClick={() => router.push('/login')} className="h-9">
                Fazer Login
              </Button>
            )}
            <Button variant="ghost" onClick={handleLogout} className="h-9">
              <LogOut className="w-4 h-4 mr-2" />
              {isLoggedIn ? 'Sair' : 'Voltar'}
            </Button>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-6 py-8">
        {/* Wizard Banner */}
        {!wizardCompleted && (
          <div className="mb-6 bg-gradient-to-r from-blue-50 to-purple-50 border border-blue-200 rounded-lg p-4 flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 bg-blue-600 rounded-lg flex items-center justify-center">
                <Wand2 className="w-5 h-5 text-white" />
              </div>
              <div>
                <h3 className="text-sm font-semibold text-gray-900">Personalize sua leitura</h3>
                <p className="text-xs text-gray-600">Ajuste automático baseado nas suas preferências</p>
              </div>
            </div>
            <Button variant="secondary" onClick={() => setShowWizard(true)} className="h-9">
              Configurar Agora
            </Button>
          </div>
        )}

        {/* Search and Upload */}
        <div className="mb-8 flex flex-col sm:flex-row gap-4 justify-between">
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-400" />
            <input
              type="search"
              placeholder="Buscar documentos..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full h-10 pl-10 pr-4 text-sm border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>
          <Button onClick={handleUpload}>
            <Upload className="w-4 h-4 mr-2" />
            Upload PDF
          </Button>
        </div>

        {/* Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {filteredDocuments.map((doc) => (
            <div key={doc.id} className="bg-white rounded-lg border border-gray-200 p-5 hover:shadow-md transition-shadow">
              <div className="flex items-start gap-3 mb-4">
                <FileText className="w-10 h-10 text-blue-600 p-2 bg-blue-50 rounded" />
                <div className="flex-1 truncate">
                  <h3 className="font-medium text-gray-900 text-sm truncate">{doc.name}</h3>
                  <p className="text-xs text-gray-500">{doc.pages} páginas • {new Date(doc.uploadDate).toLocaleDateString('pt-BR')}</p>
                </div>
              </div>
              <Button className="w-full" onClick={() => router.push(`/reader/${doc.id}`)}>
                Abrir Leitor
              </Button>
            </div>
          ))}
        </div>
      </main>

      <AccessibilityWizard
        isOpen={showWizard}
        onClose={() => setShowWizard(false)}
        onApply={(settings: AccessibilitySettings) => { // Corrigido tipagem do parâmetro
          localStorage.setItem('accessibility-settings', JSON.stringify(settings));
          localStorage.setItem('accessibility-wizard-completed', 'true');
          setWizardCompleted(true);
          setShowWizard(false);
        }}
        currentSettings={{
          fontSize: 16,
          lineSpacing: 1.5,
          columnWidth: 80,
          highContrast: false,
          focusMode: false,
          dyslexiaMode: false,
        }}
      />
    </div>
  );
}