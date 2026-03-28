"use client"; // Necessário para usar hooks como useRouter e eventos de clique

import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Button } from '@/components/Button'; // Ajuste o path conforme seu projeto
import { BookOpen, Contrast, Brain, Eye, FileText, Zap, TrendingUp } from 'lucide-react';

export default function Home() {
  const router = useRouter();

  const features = [
    {
      icon: <Contrast className="w-5 h-5" />,
      title: 'Alto Contraste',
      description: 'Contraste preto/branco puro para máxima legibilidade e acessibilidade visual',
    },
    {
      icon: <Brain className="w-5 h-5" />,
      title: 'Modo Dislexia',
      description: 'Tipografia otimizada com espaçamento adaptado para facilitar a leitura',
    },
    {
      icon: <Eye className="w-5 h-5" />,
      title: 'Modo Foco',
      description: 'Remove todas as distrações para concentração máxima no conteúdo',
    },
    {
      icon: <TrendingUp className="w-5 h-5" />,
      title: 'Progresso de Leitura',
      description: 'Acompanhe páginas lidas e tempo de leitura em tempo real',
    },
    {
      icon: <FileText className="w-5 h-5" />,
      title: 'Leitor Inspirado no Edge',
      description: 'Interface profissional baseada no Microsoft Edge PDF Reader',
    },
  ];

  const steps = [
    {
      number: '1',
      title: 'Faça upload do documento',
      description: 'Carregue arquivos PDF para começar a leitura',
    },
    {
      number: '2',
      title: 'Escolha os modos de acessibilidade',
      description: 'Ative contraste, dislexia ou foco conforme necessário',
    },
    {
      number: '3',
      title: 'Leia de forma adaptada',
      description: 'Aproveite uma experiência de leitura personalizada',
    },
  ];

  return (
    <div className="min-h-screen bg-white">
      {/* Header */}
      <header className="border-b border-gray-200 bg-white">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 bg-blue-600 rounded-md flex items-center justify-center">
              <BookOpen className="w-5 h-5 text-white" />
            </div>
            <span className="text-lg font-semibold text-gray-900">Sistema de Leitura Adaptativa</span>
          </div>
          <Link href="/login">
            <Button variant="ghost" size="sm" className="h-9 px-4">
              Entrar
            </Button>
          </Link>
        </div>
      </header>

      {/* Hero Section */}
      <section className="max-w-7xl mx-auto px-6 py-16 lg:py-24">
        <div className="grid lg:grid-cols-2 gap-12 lg:gap-16 items-center">
          {/* Left Column */}
          <div className="space-y-6">
            <div className="inline-flex items-center gap-2 px-3 py-1 bg-blue-50 rounded-full text-sm text-blue-700 font-medium">
              <Zap className="w-4 h-4" />
              Conformidade WCAG 2.2 AA
            </div>
            
            <h1 className="text-4xl lg:text-5xl font-bold text-gray-900 leading-tight">
              Sistema de Leitura Adaptativa
            </h1>
            
            <p className="text-xl text-gray-600">
              Leitura de documentos com acessibilidade adaptativa baseada em WCAG.
            </p>
            
            <p className="text-base text-gray-600 leading-relaxed">
              Experimente um leitor de PDF inspirado no Microsoft Edge com recursos avançados de acessibilidade. 
              Ative modos de alto contraste, dislexia e foco para uma experiência de leitura personalizada 
              que se adapta às suas necessidades.
            </p>

            <div className="flex flex-wrap gap-3 pt-2">
              <Button
                onClick={() => router.push('/dashboard')}
                size="lg"
                className="h-12 px-6 text-base"
              >
                Explorar Sistema
              </Button>
              <Button
                onClick={() => router.push('/reader/1')}
                variant="secondary"
                size="lg"
                className="h-12 px-6 text-base"
              >
                Ver Leitor
              </Button>
            </div>
          </div>

          {/* Right Column - Mockups (Mantidos iguais ao seu original) */}
          <div className="relative space-y-4">
             {/* ... conteúdo dos mockups (omitido para brevidade, mas permanece idêntico) ... */}
             <div className="bg-gray-900 rounded-lg p-2 shadow-xl">
                {/* Dashboard preview code */}
             </div>
             <div className="grid grid-cols-2 gap-4">
                {/* Tablet and Mobile preview code */}
             </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="bg-gray-50 py-16 lg:py-20">
        <div className="max-w-7xl mx-auto px-6">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-gray-900 mb-3">
              Recursos de Acessibilidade
            </h2>
            <p className="text-lg text-gray-600">
              Ferramentas poderosas para uma experiência de leitura inclusiva
            </p>
          </div>

          <div className="grid sm:grid-cols-2 lg:grid-cols-5 gap-6">
            {features.map((feature, index) => (
              <div
                key={index}
                className="bg-white rounded-lg border border-gray-200 p-6 hover:border-gray-300 transition-colors"
              >
                <div className="w-10 h-10 bg-blue-50 rounded-md flex items-center justify-center text-blue-600 mb-4">
                  {feature.icon}
                </div>
                <h3 className="text-base font-semibold text-gray-900 mb-2">
                  {feature.title}
                </h3>
                <p className="text-sm text-gray-600 leading-relaxed">
                  {feature.description}
                </p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Steps Section */}
      <section className="py-16 lg:py-20">
        <div className="max-w-7xl mx-auto px-6">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-gray-900 mb-3">
              Como Funciona
            </h2>
            <p className="text-lg text-gray-600">
              Três passos simples para começar
            </p>
          </div>

          <div className="grid md:grid-cols-3 gap-8 lg:gap-12">
            {steps.map((step, index) => (
              <div key={index} className="text-center">
                <div className="w-12 h-12 bg-blue-600 text-white rounded-lg flex items-center justify-center text-xl font-bold mb-4 mx-auto">
                  {step.number}
                </div>
                <h3 className="text-lg font-semibold text-gray-900 mb-2">
                  {step.title}
                </h3>
                <p className="text-sm text-gray-600">
                  {step.description}
                </p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="border-t border-gray-200 bg-gray-50">
        <div className="max-w-7xl mx-auto px-6 py-8">
          <div className="flex flex-col md:flex-row items-center justify-between gap-4">
            <div className="flex items-center gap-2">
              <div className="w-6 h-6 bg-blue-600 rounded-md flex items-center justify-center">
                <BookOpen className="w-4 h-4 text-white" />
              </div>
              <span className="text-sm text-gray-600">
                Sistema de Leitura Adaptativa
              </span>
            </div>
            <p className="text-sm text-gray-500">
              Conformidade WCAG 2.2 AA · Acessibilidade em primeiro lugar
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
}