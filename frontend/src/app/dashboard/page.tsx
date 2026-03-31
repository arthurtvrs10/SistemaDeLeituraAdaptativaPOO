"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/Button";
import {
  FileText,
  Upload,
  LogOut,
  Search,
  UserCircle,
  Wand2,
} from "lucide-react";
import { useAuth } from "@/context/AuthContext";
import { AccessibilityWizard } from "@/components/AccessibilityWizard";
import api from "@/services/api";

interface Document {
  id: string;
  name: string;
  uploadDate: string;
  pages: number;
}

interface BackendDocument {
  id: string;
  title: string;
  content: string;
}

interface AccessibilitySettings {
  fontSize: number;
  lineSpacing: number;
  columnWidth: number;
  highContrast: boolean;
  focusMode: boolean;
  dyslexiaMode: boolean;
}

const FALLBACK_DOCUMENTS: Document[] = [
  {
    id: "doc1",
    name: "WCAG em um minuto",
    uploadDate: "2026-03-20",
    pages: 2,
  },
  {
    id: "doc2",
    name: "Leitura inclusiva - demonstração",
    uploadDate: "2026-03-18",
    pages: 3,
  },
  {
    id: "doc3",
    name: "Modo visitante - documento exemplo",
    uploadDate: "2026-03-15",
    pages: 1,
  },
];

export default function Dashboard() {
  const router = useRouter();
  const { isLoggedIn, userEmail, logout } = useAuth();

  const [searchQuery, setSearchQuery] = useState("");
  const [showWizard, setShowWizard] = useState(false);

  const [wizardCompleted, setWizardCompleted] = useState<boolean>(() => {
    if (typeof window === "undefined") return false;
    return localStorage.getItem("accessibility-wizard-completed") === "true";
  });

  const [documents, setDocuments] = useState<Document[]>(FALLBACK_DOCUMENTS);
  const [isLoadingDocuments, setIsLoadingDocuments] = useState(false);

  useEffect(() => {
    async function loadDocuments() {
      if (!isLoggedIn) {
        setDocuments(FALLBACK_DOCUMENTS);
        return;
      }

      try {
        setIsLoadingDocuments(true);

        const response = await api.get("/documents");
        const backendDocuments: BackendDocument[] = response.data ?? [];

        if (!Array.isArray(backendDocuments) || backendDocuments.length === 0) {
          setDocuments(FALLBACK_DOCUMENTS);
          return;
        }

        const mappedDocuments: Document[] = backendDocuments.map((doc) => ({
          id: doc.id,
          name: doc.title,
          uploadDate: new Date().toISOString(),
          pages: Math.max(1, Math.ceil((doc.content?.length ?? 0) / 120)),
        }));

        setDocuments(mappedDocuments);
      } catch (error) {
        console.error("Erro ao carregar documentos:", error);
        setDocuments(FALLBACK_DOCUMENTS);
      } finally {
        setIsLoadingDocuments(false);
      }
    }

    loadDocuments();
  }, [isLoggedIn]);

  const filteredDocuments = documents.filter((doc) =>
    doc.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleUpload = () => {
    alert("Recurso de upload disponível em breve (Integração com Backend Java)");
  };

  const handleLogout = () => {
    logout();
    router.push("/");
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 bg-blue-600 rounded-md flex items-center justify-center">
              <FileText className="w-5 h-5 text-white" />
            </div>

            <div>
              <div className="flex items-center gap-2">
                <h1 className="text-lg font-semibold text-gray-900">
                  Sistema de Leitura
                </h1>

                {!isLoggedIn && (
                  <span className="inline-flex items-center gap-1 px-2 py-0.5 bg-amber-50 border border-amber-200 rounded text-xs text-amber-700 font-medium">
                    <UserCircle className="w-3 h-3" />
                    Modo visitante
                  </span>
                )}
              </div>

              <p className="text-sm text-gray-600">
                {isLoggedIn ? userEmail : "Seus documentos offline"}
              </p>
            </div>
          </div>

          <div className="flex items-center gap-2">
            {!isLoggedIn && (
              <Button
                variant="secondary"
                onClick={() => router.push("/login")}
                className="h-9"
              >
                Fazer Login
              </Button>
            )}

            <Button variant="ghost" onClick={handleLogout} className="h-9">
              <LogOut className="w-4 h-4 mr-2" />
              {isLoggedIn ? "Sair" : "Voltar"}
            </Button>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-6 py-8">
        {!wizardCompleted && (
          <div className="mb-6 bg-gradient-to-r from-blue-50 to-purple-50 border border-blue-200 rounded-lg p-4 flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 bg-blue-600 rounded-lg flex items-center justify-center">
                <Wand2 className="w-5 h-5 text-white" />
              </div>

              <div>
                <h3 className="text-sm font-semibold text-gray-900">
                  Personalize sua leitura
                </h3>
                <p className="text-xs text-gray-600">
                  Ajuste automático baseado nas suas preferências
                </p>
              </div>
            </div>

            <Button
              variant="secondary"
              onClick={() => setShowWizard(true)}
              className="h-9"
            >
              Configurar Agora
            </Button>
          </div>
        )}

        {isLoggedIn && (
          <div className="mb-4 rounded-lg border border-green-200 bg-green-50 px-4 py-3 text-sm text-green-800">
            Documentos carregados do backend quando disponíveis. Se a API não
            responder ou a lista vier vazia, o sistema usa documentos de demonstração.
          </div>
        )}

        {!isLoggedIn && (
          <div className="mb-4 rounded-lg border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-800">
            Você está em modo visitante. Os documentos exibidos abaixo são de
            demonstração.
          </div>
        )}

        <div className="mb-8 flex flex-col sm:flex-row gap-4 justify-between">
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
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

        {isLoadingDocuments && (
          <div className="mb-6 text-sm text-gray-600">Carregando documentos...</div>
        )}

        {filteredDocuments.length === 0 ? (
          <div className="bg-white rounded-lg border border-gray-200 p-8 text-center text-sm text-gray-500">
            Nenhum documento encontrado.
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {filteredDocuments.map((doc) => (
              <div
                key={doc.id}
                className="bg-white rounded-lg border border-gray-200 p-5 hover:shadow-md transition-shadow"
              >
                <div className="flex items-start gap-3 mb-4">
                  <FileText className="w-10 h-10 text-blue-600 p-2 bg-blue-50 rounded" />

                  <div className="flex-1 min-w-0">
                    <h3 className="font-medium text-gray-900 text-sm truncate">
                      {doc.name}
                    </h3>
                    <p className="text-xs text-gray-500">
                      {doc.pages} páginas •{" "}
                      {new Date(doc.uploadDate).toLocaleDateString("pt-BR")}
                    </p>
                    <p className="text-[11px] text-gray-400 mt-1 truncate">
                      ID: {doc.id}
                    </p>
                  </div>
                </div>

                <Button
                  className="w-full"
                  onClick={() => router.push(`/reader/${doc.id}`)}
                >
                  Abrir Leitor
                </Button>
              </div>
            ))}
          </div>
        )}
      </main>

      <AccessibilityWizard
        isOpen={showWizard}
        onClose={() => setShowWizard(false)}
        onApply={(settings: AccessibilitySettings) => {
          localStorage.setItem(
            "accessibility-settings",
            JSON.stringify(settings)
          );
          localStorage.setItem("accessibility-wizard-completed", "true");
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