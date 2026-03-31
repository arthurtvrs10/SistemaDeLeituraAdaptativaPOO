"use client";

import { X } from 'lucide-react';
import { Button } from './Button';
import { useRouter } from 'next/navigation'; // Importação alterada
import { useAuth } from '@/context/AuthContext';

export function LoginPromptModal() {
  const router = useRouter(); // Hook alterado
  const { showLoginPrompt, setShowLoginPrompt } = useAuth();

  if (!showLoginPrompt) return null;

  const handleLoginClick = () => {
    setShowLoginPrompt(false);
    router.push('/login'); // Método push() do Next.js
  };

  const handleClose = () => {
    setShowLoginPrompt(false);
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl max-w-md w-full">
        {/* ... (Header e Conteúdo idênticos) ... */}
        <div className="flex gap-3 p-6 bg-gray-50 rounded-b-lg">
          <Button variant="secondary" onClick={handleClose} className="flex-1">
            Continuar sem login
          </Button>
          <Button onClick={handleLoginClick} className="flex-1">
            Fazer Login
          </Button>
        </div>
      </div>
    </div>
  );
}