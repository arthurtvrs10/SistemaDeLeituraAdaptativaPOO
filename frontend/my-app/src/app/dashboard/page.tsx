"use client";

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Button } from '@/components/Button';
import { FileText, Upload, LogOut, Search, UserCircle, Wand2 } from 'lucide-react';
import { useAuth } from '@/context/AuthContext';
import { AccessibilityWizard } from '@/components/AccessibilityWizard';

export default function Dashboard() {
  const router = useRouter();
  const { isLoggedIn, userEmail, logout } = useAuth();
  const [searchQuery, setSearchQuery] = useState('');
  const [showWizard, setShowWizard] = useState(false);
  const [wizardCompleted, setWizardCompleted] = useState(false);

  useEffect(() => {
    setWizardCompleted(localStorage.getItem('accessibility-wizard-completed') === 'true');
  }, []);

  const documents = [
    { id: '1', name: 'Introdução à Acessibilidade Web.pdf', uploadDate: '2026-03-20', pages: 45 },
    { id: '2', name: 'WCAG 2.2 Diretrizes Completas.pdf', uploadDate: '2026-03-18', pages: 120 },
    { id: '3', name: 'Design Inclusivo - Guia Prático.pdf', uploadDate: '2026-03-15', pages: 78 },
    { id: '4', name: 'Tecnologias Assistivas.pdf', uploadDate: '2026-03-10', pages: 56 },
  ];

  const filteredDocuments = documents.filter(doc =>
    doc.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header, CTA e Actions Bar mantidos iguais, alterando apenas navegação */}
      <header className="bg-white border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
           {/* ... conteúdo do header ... */}
           <Button variant="ghost" onClick={() => { logout(); router.push('/'); }}>
             <LogOut className="w-4 h-4 mr-2" />
             {isLoggedIn ? 'Sair' : 'Voltar'}
           </Button>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-6 py-8">
        {/* ... Grid de documentos ... */}
        {filteredDocuments.map((doc) => (
          <div key={doc.id} className="bg-white rounded-lg border p-5">
            {/* ... info do doc ... */}
            <Button className="w-full" onClick={() => router.push(`/reader/${doc.id}`)}>
              Abrir Documento
            </Button>
          </div>
        ))}
      </main>

      <AccessibilityWizard
        isOpen={showWizard}
        onClose={() => setShowWizard(false)}
        onApply={(settings) => {
          localStorage.setItem('accessibility-settings', JSON.stringify(settings));
          setShowWizard(false);
          alert('Configurações aplicadas!');
        }}
        currentSettings={{ fontSize: 16, lineSpacing: 1.5, columnWidth: 80, highContrast: false, focusMode: false, dyslexiaMode: false }}
      />
    </div>
  );
}