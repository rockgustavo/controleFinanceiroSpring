CREATE TABLE dados_mercado (
    id          UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
    snapshot_id UUID          NOT NULL UNIQUE REFERENCES snapshots(id),
    selic       NUMERIC(6,2),
    usd_brl     NUMERIC(8,4),
    ibovespa    NUMERIC(12,2),
    ivvb11      NUMERIC(10,4),
    ipca        NUMERIC(6,2),
    buscado_em  TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);
