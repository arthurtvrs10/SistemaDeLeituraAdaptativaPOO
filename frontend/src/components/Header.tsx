"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { removeToken } from "@/lib/auth";

export default function Header() {
  const router = useRouter();

  function handleLogout() {
    removeToken();
    router.push("/login");
  }

  return (
    <header className="border-b bg-white">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">
        <div>
          <h1 className="text-xl font-bold text-slate-800">Sistema de Leitura Adaptativa</h1>
          <p className="text-sm text-slate-500">Leitura acessível e personalizada</p>
        </div>

        <nav className="flex items-center gap-3">
          <Link
            href="/documents"
            className="rounded-xl border px-4 py-2 text-sm font-medium hover:bg-slate-50"
          >
            Documentos
          </Link>
          <button
            onClick={handleLogout}
            className="rounded-xl bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:opacity-90"
          >
            Sair
          </button>
        </nav>
      </div>
    </header>
  );
}