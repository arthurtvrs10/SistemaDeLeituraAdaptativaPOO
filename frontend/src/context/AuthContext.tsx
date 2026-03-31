"use client";

import { createContext, useContext, useState, ReactNode } from "react";
import api from "@/services/api";

interface AuthContextType {
  isLoggedIn: boolean;
  userEmail: string | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  showLoginPrompt: boolean;
  setShowLoginPrompt: (show: boolean) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

function getInitialAuth() {
  if (typeof window === "undefined") {
    return {
      isLoggedIn: false,
      userEmail: null as string | null,
    };
  }

  const savedEmail = localStorage.getItem("user-email");
  const savedToken = localStorage.getItem("auth-token");

  return {
    isLoggedIn: Boolean(savedEmail && savedToken),
    userEmail: savedEmail,
  };
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const initialAuth = getInitialAuth();

  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(initialAuth.isLoggedIn);
  const [userEmail, setUserEmail] = useState<string | null>(initialAuth.userEmail);
  const [showLoginPrompt, setShowLoginPrompt] = useState(false);

  const login = async (email: string, password: string) => {
    const response = await api.post("/auth/login", {
      email,
      password,
    });

    const token = response.data?.token;

    if (!token) {
      throw new Error("Token não recebido no login.");
    }

    localStorage.setItem("user-email", email);
    localStorage.setItem("auth-token", token);

    setIsLoggedIn(true);
    setUserEmail(email);
  };

  const logout = () => {
    localStorage.removeItem("user-email");
    localStorage.removeItem("auth-token");

    setIsLoggedIn(false);
    setUserEmail(null);
  };

  return (
    <AuthContext.Provider
      value={{
        isLoggedIn,
        userEmail,
        login,
        logout,
        showLoginPrompt,
        setShowLoginPrompt,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }

  return context;
}