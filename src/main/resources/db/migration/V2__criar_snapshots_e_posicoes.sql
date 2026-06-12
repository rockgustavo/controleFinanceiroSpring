CREATE TABLE snapshots (
    id         UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    data       DATE        NOT NULL UNIQUE,
    observacoes TEXT,
    criado_em  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE posicoes (
    id          UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    snapshot_id UUID           NOT NULL REFERENCES snapshots(id),
    ativo_id    UUID           NOT NULL REFERENCES ativos(id),
    quantidade  NUMERIC(18,8)  NOT NULL,
    preco_unit  NUMERIC(18,4)  NOT NULL,
    total_bruto NUMERIC(18,4)  NOT NULL,
    total_liq   NUMERIC(18,4)  NOT NULL,
    taxa        VARCHAR(50),
    CONSTRAINT uq_posicao_snapshot_ativo UNIQUE (snapshot_id, ativo_id)
);

CREATE INDEX idx_posicoes_snapshot ON posicoes (snapshot_id);
