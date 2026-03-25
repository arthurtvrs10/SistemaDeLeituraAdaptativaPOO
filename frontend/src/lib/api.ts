import { getToken } from "./auth";
import type { DocumentItem, LoginRequest, LoginResponse, ReadingResponse } from "./types";

const API_BASE_URL = "http://localhost:8080";

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const token = getToken();

  const headers: HeadersInit = {
    "Content-Type": "application/json",
    ...(options.headers || {}),
  };

  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers,
    cache: "no-store",
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || `Erro ${response.status}`);
  }

  return response.json() as Promise<T>;
}

export async function login(data: LoginRequest): Promise<LoginResponse> {
  return request<LoginResponse>("/api/auth/login", {
    method: "POST",
    body: JSON.stringify(data),
  });
}

export async function fetchDocuments(): Promise<DocumentItem[]> {
  return request<DocumentItem[]>("/api/documents");
}

export async function fetchReading(documentId: string, page?: number): Promise<ReadingResponse> {
  const query = page ? `?page=${page}` : "";
  return request<ReadingResponse>(`/api/reading/${documentId}${query}`);
}