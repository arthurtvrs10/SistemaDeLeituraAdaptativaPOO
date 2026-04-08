"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import api from "@/services/api";

export default function RegisterPage() {
  const router = useRouter();

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  async function handleRegister(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();

    setError("");
    setSuccess("");

    if (!name || !email || !password || !confirmPassword) {
      setError("Preencha todos os campos.");
      return;
    }

    if (password !== confirmPassword) {
      setError("As senhas não coincidem.");
      return;
    }

    try {
      setLoading(true);

      await api.post("/auth/register", {
        name,
        email,
        password,
      });

      setSuccess("Cadastro realizado com sucesso!");

      setTimeout(() => {
        router.push("/login");
      }, 1200);
    } catch (err: unknown) {
      let message = "Erro ao cadastrar usuário.";

      if ( typeof err === "object" && err !== null) {
        const error = err as {
          response?: {
            data?: {
              message?: string;
            } | string;
          }
        }
        
        if (typeof error.response?.data === "string") {
          message = error.response.data;
        } else if (typeof error.response?.data?.message === "string") {
          message = error.response.data.message;
        }
      }
      setError(message);  
    }    
  }

  return (
    <main className="min-h-screen flex items-center justify-center bg-neutral-950 px-4">
      <div className="w-full max-w-md rounded-2xl bg-neutral-900 border border-neutral-800 shadow-2xl p-8">
        <h1 className="text-2xl font-bold text-white mb-2">Criar conta</h1>
        <p className="text-sm text-neutral-400 mb-6">
          Faça seu cadastro para salvar suas preferências de acessibilidade.
        </p>

        <form onSubmit={handleRegister} className="space-y-4">
          <div>
            <label className="block text-sm text-neutral-300 mb-1">
              Nome
            </label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Seu nome"
              className="w-full rounded-lg border border-neutral-700 bg-neutral-800 px-4 py-3 text-white outline-none focus:border-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm text-neutral-300 mb-1">
              E-mail
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="seuemail@exemplo.com"
              className="w-full rounded-lg border border-neutral-700 bg-neutral-800 px-4 py-3 text-white outline-none focus:border-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm text-neutral-300 mb-1">
              Senha
            </label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="********"
              className="w-full rounded-lg border border-neutral-700 bg-neutral-800 px-4 py-3 text-white outline-none focus:border-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm text-neutral-300 mb-1">
              Confirmar senha
            </label>
            <input
              type="password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              placeholder="********"
              className="w-full rounded-lg border border-neutral-700 bg-neutral-800 px-4 py-3 text-white outline-none focus:border-blue-500"
            />
          </div>

          {error && (
            <div className="rounded-lg bg-red-500/10 border border-red-500/30 px-4 py-3 text-sm text-red-300">
              {error}
            </div>
          )}

          {success && (
            <div className="rounded-lg bg-green-500/10 border border-green-500/30 px-4 py-3 text-sm text-green-300">
              {success}
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full rounded-lg bg-blue-600 hover:bg-blue-700 transition px-4 py-3 font-medium text-white disabled:opacity-60"
          >
            {loading ? "Cadastrando..." : "Cadastrar"}
          </button>
        </form>

        <button
          type="button"
          onClick={() => router.push("/login")}
          className="mt-4 w-full text-sm text-neutral-400 hover:text-white transition"
        >
          Já tem conta? Entrar
        </button>
      </div>
    </main>
  );
}