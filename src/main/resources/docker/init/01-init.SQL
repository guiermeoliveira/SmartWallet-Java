-- 💡 Este script roda automaticamente na PRIMEIRA vez que o container PostgreSQL sobe.
-- Usamos para habilitar extensões que precisamos desde o início.

-- pgvector: extensão que adiciona suporte a vetores (embeddings) no PostgreSQL.
-- É o que permite fazer RAG (busca semântica por similaridade) sem precisar
-- de um banco vetorial separado como Pinecone no MVP.
CREATE EXTENSION IF NOT EXISTS vector;

-- uuid-ossp: gera UUIDs automaticamente no banco.
-- Usaremos UUIDs como IDs das entidades em vez de integers sequenciais.
-- Motivo: mais seguro (não expõe contagem de registros), melhor para sistemas distribuídos.
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- pg_trgm: suporte a busca por similaridade de texto (trigramas).
-- Útil para buscas do tipo "LIKE %termo%" com performance em colunas indexadas.
CREATE EXTENSION IF NOT EXISTS pg_trgm;