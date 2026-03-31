"use client";

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { useAuth } from '@/context/AuthContext';
import { Button } from '@/components/Button';
import { Input } from '@/components/Input';
import { Card } from '@/components/Card';
import { BookOpen } from 'lucide-react';

export default function Login() {
  const router = useRouter();
  const { login } = useAuth();
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });

  const [error, setError] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();
  setError("");

  try {
    await login(formData.email, formData.password);
    console.log("TOKEN APÓS LOGIN:", localStorage.getItem("auth-token"));
    router.push("/dashboard");
  } catch (err) {
    console.error("Falha no login:", err);
    setError("Email ou senha inválidos, ou o backend não está disponível.");
  }
};

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
      <Card className="w-full max-w-md p-8">
        <div className="text-center mb-6">
          <div className="inline-flex items-center justify-center w-12 h-12 bg-blue-600 rounded-lg mb-4">
            <BookOpen className="w-6 h-6 text-white" />
          </div>
          <h1 className="text-xl font-semibold text-gray-900">Acesse sua Conta</h1>
        </div>

        <form onSubmit={handleSubmit} className="space-y-5 text-gray-900">
          <Input
            label="Email"
            type="email"
            value={formData.email}
            onChange={(e) => setFormData({ ...formData, email: e.target.value })}
            required
          />
          <Input
            label="Senha"
            type="password"
            value={formData.password}
            onChange={(e) => setFormData({ ...formData, password: e.target.value })}
            required
          />
          <Button type="submit" className="w-full">Entrar</Button>
          
          {error && <p className="mt-4 text-sm text-red-600">{error}</p>}
        </form>

        

        <div className="mt-4 text-center">
           <Link href="/dashboard" className="text-sm text-gray-500 hover:text-blue-600">
             Continuar sem login
           </Link>
        </div>
      </Card>
    </div>
  );
}