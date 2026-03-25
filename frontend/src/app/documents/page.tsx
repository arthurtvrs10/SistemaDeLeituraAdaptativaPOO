"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import Header from "@/components/Header";
import { fetchDocuments } from "@/lib/api";
import { isAuthenticated } from "@/lib/auth";
import { useRouter } from "next/navigation";
import type { DocumentItem } from "@/lib/types";

export default function DocumentsPage() {
  const router = useRouter();
  const [documents, setDocuments] = useState<DocumentItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!isAuthenticated()) {
      router.push("/login");
      return;
    }

    async function loadDocuments() {
      try {
        const data = await fetchDocuments();
        setDocuments(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Erro ao carregar documentos.");
      } finally {
        setLoading(false);
      }
    }

    loadDocuments();
  }, [router]);

  return (
    <main className="min-h-screen bg-slate-100">
      <Header />

      <section className="mx-auto max-w-6xl px-6 py-8">
        <h2 className="mb-2 text-3xl font-bold text-slate-900">Documentos disponíveis</h2>
        <p className="mb-8 text-slate-600">
          Escolha um documento para iniciar sua leitura adaptativa.
        </p>

        {loading && <p>Carregando documentos...</p>}

        {error && (
          <div className="rounded-2xl border border-red-200 bg-red-50 p-4 text-red-700">
            {error}
          </div>
        )}

        <div className="grid gap-4 md:grid-cols-2">
          {documents.map((doc) => (
            <article key={doc.id} className="rounded-2xl bg-white p-6 shadow-sm">
              <h3 className="text-xl font-semibold text-slate-900">{doc.title}</h3>
              <p className="mt-2 text-sm text-slate-500">ID: {doc.id}</p>

              <Link
                href={`/reader/${doc.id}`}
                className="mt-5 inline-block rounded-xl bg-slate-900 px-4 py-2 font-medium text-white hover:opacity-90"
              >
                Abrir leitura
              </Link>
            </article>
          ))}
        </div>
      </section>
    </main>
  );
}