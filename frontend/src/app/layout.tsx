import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { AuthProvider } from "@/context/AuthContext";
import { LoginPromptModal } from "@/components/LoginPromptModal";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Sistema de Leitura Adaptativa",
  description: "Interface acessível inspirada no Microsoft Edge",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="pt-BR">
      <body>
        <AuthProvider>
          {children}
          <LoginPromptModal />
        </AuthProvider>
      </body>
    </html>
  );
}