"use client";

import Link from 'next/link';
import { useRouter } from 'next/navigation';
// ... outros imports

export default function Login() {
  const router = useRouter();
  const { login } = useAuth();
  // ... lógica de formulário

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    login(formData.email);
    router.push('/dashboard');
  };

  return (
    // ... JSX do Card
    <p className="text-center mt-6 text-sm">
      Não tem uma conta?{' '}
      <Link href="/register" className="text-blue-600 hover:underline">
        Cadastre-se
      </Link>
    </p>
  );
}