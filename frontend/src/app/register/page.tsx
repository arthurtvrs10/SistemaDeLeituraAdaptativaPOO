"use client";

import { createContext, useContext, useState, ReactNode, useEffect } from 'react';

interface AuthContextType {
  isLoggedIn: boolean;
  userEmail: string | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  showLoginPrompt: boolean;
  setShowLoginPrompt: (show: boolean) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  // Inicialização Lazy: O React executa estas funções apenas UMA vez no carregamento
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(() => {
    if (typeof window !== 'undefined') {
      return !!localStorage.getItem('user-email');
    }
    return false;
  });

  const [userEmail, setUserEmail] = useState<string | null>(() => {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('user-email');
    }
    return null;
  });

  const [showLoginPrompt, setShowLoginPrompt] = useState(false);
  const [isLoaded, setIsLoaded] = useState(false);

  // useEffect agora serve apenas para dizer que o componente montou no cliente
  // Isso evita erros de hidratação entre o servidor e o cliente
  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    setIsLoaded(true);
  }, []);

  const login = async (email: string, password: string) => {
    // Simulação de delay de rede ou chamada ao backend Java
    setIsLoggedIn(true);
    setUserEmail(email);
    localStorage.setItem('user-email', email);
  };

  const logout = () => {
    setIsLoggedIn(false);
    setUserEmail(null);
    localStorage.removeItem('user-email');
  };

  // Importante: No Next.js, não renderize nada até que o carregamento do cliente termine
  // para evitar erro de "Hydration failed"
  if (!isLoaded) {
    return null; 
  }

  return (
    <AuthContext.Provider value={{ isLoggedIn, userEmail, login, logout, showLoginPrompt, setShowLoginPrompt }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
}