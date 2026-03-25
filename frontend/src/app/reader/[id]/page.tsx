"use client";

import { useEffect, useMemo, useState } from "react";
import { useParams, useRouter, useSearchParams } from "next/navigation";
import Header from "@/components/Header";
import ReadingControls from "@/components/ReadingControls";
import { fetchReading } from "@/lib/api";
import { isAuthenticated } from "@/lib/auth";
import type { AccessibilityProfile, ReadingResponse } from "@/lib/types";

function resolveReaderStyle(profile?: AccessibilityProfile): React.CSSProperties {
  const fontSize = profile?.fontSize ?? 18;
  const lineHeight = profile?.lineHeight ?? 1.6;
  const columnWidth = profile?.columnWidth ?? 80;
  const theme = profile?.theme ?? "LIGHT";
  const focusMode = profile?.focusMode ?? false;
  const dyslexiaFont = profile?.dyslexiaFriendlyFont ?? false;

  const isDark = theme === "DARK";
  const isHighContrast = theme === "HIGH_CONTRAST";

  return {
    fontSize: `${fontSize}px`,
    lineHeight: String(lineHeight),
    maxWidth: `${columnWidth}ch`,
    margin: "0 auto",
    padding: "2rem",
    borderRadius: "24px",
    background: isHighContrast ? "#000000" : isDark ? "#111827" : "#ffffff",
    color: isHighContrast ? "#ffff00" : isDark ? "#f9fafb" : "#111827",
    boxShadow: focusMode ? "0 10px 30px rgba(0,0,0,0.18)" : "0 4px 12px rgba(0,0,0,0.08)",
    fontFamily: dyslexiaFont ? "Arial, Verdana, sans-serif" : "Georgia, serif",
    outline: focusMode ? "3px solid #94a3b8" : "none",
  };
}

export default function ReaderPage() {
  const params = useParams<{ id: string }>();
  const router = useRouter();
  const searchParams = useSearchParams();

  const documentId = params.id;
  const pageFromUrl = Number(searchParams.get("page") || "");

  const [data, setData] = useState<ReadingResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadReading(page?: number) {
    try {
      setLoading(true);
      setError("");
      const result = await fetchReading(documentId, page);
      setData(result);
    } catch (err) {
      setData(null);
      setError(err instanceof Error ? err.message : "Erro ao carregar leitura.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    if (!isAuthenticated()) {
      router.push("/login");
      return;
    }

    if (pageFromUrl && pageFromUrl > 0) {
      loadReading(pageFromUrl);
    } else {
      loadReading();
    }
  }, [documentId, pageFromUrl, router]);

  const readerStyle = useMemo(() => resolveReaderStyle(data?.profile), [data?.profile]);

  function goToPage(page: number) {
    router.push(`/reader/${documentId}?page=${page}`);
  }

  return (
    <main className="min-h-screen bg-slate-100">
      <Header />

      <section className="mx-auto max-w-6xl px-6 py-8">
        {loading && <p>Carregando leitura...</p>}

        {error && (
          <div className="rounded-2xl border border-red-200 bg-red-50 p-4 text-red-700">
            {error}
          </div>
        )}

        {data && !loading && (
          <>
            <div className="mb-6">
              <h2 className="text-3xl font-bold text-slate-900">
                {data.documentTitle}
              </h2>
              <p className="mt-2 text-slate-600">
                Documento: <strong>{documentId}</strong>
              </p>
            </div>

            <article style={readerStyle}>
              <div className="space-y-3">
                {data.lines.map((line, index) => (
                  <p key={index}>{line}</p>
                ))}
              </div>
            </article>

            <ReadingControls
              currentPage={data.currentPage}
              totalPages={data.totalPages}
              onPrevious={() => {
                if (data.hasPrevious) {
                  goToPage(data.currentPage - 1);
                }
              }}
              onNext={() => {
                if (data.hasNext) {
                  goToPage(data.currentPage + 1);
                }
              }}
            />
          </>
        )}
      </section>
    </main>
  );
}