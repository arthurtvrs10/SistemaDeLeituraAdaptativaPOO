# Atualização da Documentação — Refatoração do Backend MVC

## Projeto

Sistema de Leitura Adaptativa

## Módulo

Backend MVC com Java + Spring Boot

## Situação atual da refatoração

Nesta etapa do projeto, o backend foi refatorado com foco na leitura de arquivos PDF. A nova estrutura foi criada utilizando Java, Spring Boot e arquitetura MVC, com separação entre Controller, Service, Repository e Model.

O sistema foi simplificado para remover temporariamente funcionalidades que aumentavam a complexidade inicial, como autenticação, JWT, usuários e progresso individual de leitura.

---

# 1. Objetivo da versão atual

O objetivo desta versão é permitir que o sistema gerencie documentos PDF de forma simples, permitindo o envio, armazenamento local, consulta, renderização de páginas e exclusão dos arquivos.

A prioridade desta etapa é consolidar a base do backend antes de avançar para as funcionalidades principais do projeto, como acessibilidade e adaptação da leitura.

---

# 2. Funcionalidades implementadas

Nesta versão, o sistema contempla as seguintes funcionalidades:

```txt
RF01 — Realizar upload de PDF
RF02 — Salvar cópia do PDF no servidor
RF03 — Registrar metadados do documento
RF04 — Listar documentos cadastrados
RF05 — Buscar documento por ID
RF06 — Renderizar página do PDF como imagem PNG
RF07 — Excluir documento
```

---

# 3. Funcionalidades removidas temporariamente

As seguintes funcionalidades não serão implementadas nesta versão inicial:

```txt
Autenticação
JWT
Cadastro de usuários
Login
Controle de permissões
Progresso de leitura por usuário
Anotações
Marcações
Estatísticas avançadas
Recomendações adaptativas
```

Essas funcionalidades poderão ser retomadas futuramente, após a consolidação da base do sistema.

---

# 4. Decisão sobre o progresso de leitura

A funcionalidade de progresso de leitura foi analisada, mas não será implementada nesta versão.

## Justificativa

Como a autenticação foi removida temporariamente, o sistema não possui identificação de usuário. Dessa forma, não há como associar corretamente a última página lida a uma pessoa específica.

Caso o progresso fosse implementado agora, ele seria vinculado apenas ao documento, por exemplo:

```txt
Documento 1 → última página lida: 5
```

Essa abordagem não representa adequadamente o comportamento real do sistema, pois diferentes usuários poderiam utilizar o mesmo documento e sobrescrever o progresso um do outro.

Por esse motivo, o progresso de leitura será tratado como funcionalidade futura.

## Decisão

```txt
O progresso de leitura não será implementado nesta versão da refatoração.
```

## Evolução futura

Quando a autenticação ou identificação do usuário for adicionada, o progresso poderá ser implementado da seguinte forma:

```txt
Usuário + Documento → Última página lida
```

Exemplo:

```txt
Usuário 1 + Documento 1 → Página 5
Usuário 2 + Documento 1 → Página 12
```

---

# 5. Arquitetura atual

A estrutura atual do backend segue o padrão MVC.

```txt
controller
service
repository
model
```

## Controller

Responsável por receber as requisições HTTP e encaminhá-las para a camada de serviço.

Exemplo:

```txt
DocumentController
```

## Service

Responsável por concentrar as regras de negócio.

Exemplo:

```txt
DocumentService
PdfService
```

## Repository

Responsável pela comunicação com o banco de dados.

Exemplo:

```txt
DocumentRepository
```

## Model

Responsável por representar as entidades persistidas no banco.

Exemplo:

```txt
Document
```

---

# 6. Fluxo principal do upload de PDF

```txt
Cliente envia PDF via multipart/form-data
        ↓
DocumentController recebe title e file
        ↓
DocumentService valida o arquivo
        ↓
DocumentService salva uma cópia do PDF em uploads/pdfs
        ↓
PdfService conta a quantidade de páginas
        ↓
DocumentService cria o objeto Document
        ↓
DocumentRepository salva os metadados no banco
        ↓
API retorna os dados do documento cadastrado
```

---

# 7. Fluxo principal da renderização de página

```txt
Cliente solicita uma página do PDF
        ↓
DocumentController recebe id e pageNumber
        ↓
DocumentService busca o documento no banco
        ↓
DocumentService localiza o arquivo físico pelo filePath
        ↓
PdfService abre o PDF com Apache PDFBox
        ↓
PdfService renderiza a página como imagem PNG
        ↓
API retorna a imagem para o cliente
```

---

# 8. Justificativa para uso de multipart/form-data

A rota de upload utiliza `multipart/form-data` porque o sistema precisa receber um arquivo PDF real junto com dados textuais do documento.

O JSON não é adequado para esse caso, pois enviaria apenas textos, por exemplo:

```json
{
  "title": "Apostila Java",
  "file": "teste.pdf"
}
```

Nesse exemplo, `"teste.pdf"` seria apenas o nome do arquivo, e não o conteúdo real do PDF.

Com `multipart/form-data`, a requisição é enviada em partes:

```txt
Parte 1 → title, campo de texto
Parte 2 → file, arquivo PDF real
```

No Spring Boot, esses dados são recebidos com:

```txt
@RequestParam("title")
@RequestParam("file")
```

---

# 9. Justificativa para salvar PDF em pasta local

O arquivo PDF será salvo em uma pasta local no servidor porque essa abordagem reduz a complexidade da aplicação nesta fase.

O banco de dados armazenará apenas os metadados do documento, como:

```txt
id
title
originalFileName
filePath
totalPages
uploadedAt
```

O conteúdo binário do PDF não será salvo no banco.

Essa decisão facilita o uso do Apache PDFBox, pois o backend pode localizar o arquivo pelo caminho salvo e abrir o PDF diretamente para contar páginas ou renderizar uma página específica.

---

# 10. Endpoints atuais

## Upload de PDF

```txt
POST /api/documents/upload
```

Utilizado para enviar um novo PDF ao sistema.

## Listagem de documentos

```txt
GET /api/documents
```

Utilizado para listar todos os documentos cadastrados.

## Busca por ID

```txt
GET /api/documents/{id}
```

Utilizado para buscar os dados de um documento específico.

## Renderização de página

```txt
GET /api/documents/{id}/page/{pageNumber}
```

Utilizado para renderizar uma página específica do PDF como imagem PNG.

## Exclusão de documento

```txt
DELETE /api/documents/{id}
```

Utilizado para excluir o registro do documento e o arquivo físico salvo no servidor.

---

# 11. Próxima etapa do projeto

Após concluir a base de documentos PDF, a próxima etapa será retomar a parte principal do Sistema de Leitura Adaptativa: as configurações de acessibilidade.

Essa etapa deverá permitir que o sistema aplique preferências de leitura, como:

```txt
Tamanho da fonte
Espaçamento
Modo de contraste
Tema visual
Preferências de leitura
Configuração adaptativa da exibição
```

Além disso, essa etapa será utilizada para aplicar conceitos obrigatórios de Java e Programação Orientada a Objetos.

---

# 12. Conceitos obrigatórios que serão aplicados no código

Na próxima etapa, o projeto deverá incluir obrigatoriamente os seguintes conceitos:

```txt
Classe abstrata
Threads
Sobrecarga de construtor
Sobrescrita de métodos
try...catch
```

Esses conceitos deverão ser aplicados de forma coerente com a regra de negócio do sistema, evitando implementações artificiais.

---

# 13. Planejamento da próxima etapa

## Parte 11 — Configurações de acessibilidade

Criar a estrutura responsável por armazenar e aplicar configurações de acessibilidade na leitura.

Possíveis classes:

```txt
AccessibilityProfile
AccessibilityService
AccessibilityController
AccessibilityRepository
```

## Parte 12 — Classe abstrata

Criar uma classe abstrata para representar uma configuração genérica de leitura ou uma estratégia de adaptação.

Exemplo conceitual:

```txt
ReadingConfiguration
```

Essa classe poderá definir comportamentos comuns para diferentes configurações de acessibilidade.

## Parte 13 — Sobrescrita de métodos

Criar classes específicas que herdam de uma classe abstrata e sobrescrevem métodos para aplicar comportamentos diferentes.

Exemplo:

```txt
HighContrastConfiguration
LargeFontConfiguration
DefaultReadingConfiguration
```

## Parte 14 — Sobrecarga de construtor

Permitir criar perfis de acessibilidade de formas diferentes.

Exemplo conceitual:

```txt
Perfil criado apenas com nome
Perfil criado com nome e tamanho da fonte
Perfil criado com nome, tamanho da fonte e contraste
```

## Parte 15 — try...catch

Aplicar tratamento de erros em operações críticas, como:

```txt
Salvar configurações
Aplicar preferências
Processar documento
Renderizar página
Validar dados recebidos
```

## Parte 16 — Threads

Utilizar threads para executar tarefas secundárias sem bloquear o fluxo principal da API.

Exemplos possíveis:

```txt
Processar métricas de leitura em segundo plano
Registrar logs de acessibilidade
Preparar configurações adaptativas
Executar processamento posterior ao upload do PDF
```

---

# 14. Resumo da decisão atual

A refatoração atual será encerrada com foco em documentos PDF.

O progresso de leitura não será implementado agora, pois depende de uma definição futura sobre autenticação, usuário ou sessão.

A próxima fase do projeto será dedicada às configurações de acessibilidade, que representam a parte principal do Sistema de Leitura Adaptativa.

Essa próxima fase também será usada para aplicar os conceitos obrigatórios de Java e POO exigidos no projeto.
