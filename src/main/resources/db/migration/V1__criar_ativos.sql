CREATE TABLE ativos (
    id           UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    nome         VARCHAR(100) NOT NULL,
    tipo         VARCHAR(20)  NOT NULL CHECK (tipo IN ('RENDA_FIXA','RENDA_VARIAVEL','FII','ETF')),
    ticker       VARCHAR(10),
    observacoes  TEXT,
    criado_em    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    arquivado_em TIMESTAMPTZ
);

CREATE UNIQUE INDEX uq_ativos_ticker ON ativos (ticker) WHERE arquivado_em IS NULL AND ticker IS NOT NULL;
CREATE INDEX idx_ativos_tipo ON ativos (tipo) WHERE arquivado_em IS NULL;
