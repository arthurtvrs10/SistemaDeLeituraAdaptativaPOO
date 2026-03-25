"use client";

interface Props {
  currentPage: number;
  totalPages: number;
  onPrevious: () => void;
  onNext: () => void;
}

export default function ReadingControls({
  currentPage,
  totalPages,
  onPrevious,
  onNext,
}: Props) {
  return (
    <div className="mt-6 flex items-center justify-between gap-4 rounded-2xl border bg-white p-4 shadow-sm">
      <button
        onClick={onPrevious}
        disabled={currentPage <= 1}
        className="rounded-xl border px-4 py-2 disabled:cursor-not-allowed disabled:opacity-50"
      >
        Página anterior
      </button>

      <span className="text-sm font-medium text-slate-700">
        Página {currentPage} de {totalPages}
      </span>

      <button
        onClick={onNext}
        disabled={currentPage >= totalPages}
        className="rounded-xl bg-slate-900 px-4 py-2 text-white disabled:cursor-not-allowed disabled:opacity-50"
      >
        Próxima página
      </button>
    </div>
  );
}