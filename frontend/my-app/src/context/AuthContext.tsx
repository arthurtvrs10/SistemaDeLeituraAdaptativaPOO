"use client";

import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';

// 1. Criamos uma interface para o Usuário (Resolve o erro @typescript-eslint/no-explicit-any)
interface UserData {
  id: string;
  name: string;
  email: string;
  token?: string;
}

interface AuthContextType {
  user: UserData | null;
  login: (data: UserData) => void;
  logout: () => void;
  isAuthenticated: boolean;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<UserData | null>(null);

  // 2. Resolvemos o erro react-hooks/set-state-in-effect
  // No Next.js, o localStorage só está disponível no cliente.
  useEffect(() => {
    const initializeAuth = () => {
      try {
        const savedUser = localStorage.getItem('user');
        if (savedUser) {
          const parsedUser: UserData = JSON.parse(savedUser);
          setUser(parsedUser);
        }
      } catch (error) {
        console.error("Erro ao sincronizar autenticação:", error);
      }
    };

    initializeAuth();
  }, []); // Executa apenas uma vez após a montagem do componente no cliente

  const login = (data: UserData) => {
    setUser(data);
    localStorage.setItem('user', JSON.stringify(data));
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('user');
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, isAuthenticated: !!user }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth deve ser usado dentro de um AuthProvider");
  }
  return context;
};