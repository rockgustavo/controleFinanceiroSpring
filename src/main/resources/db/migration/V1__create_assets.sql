CREATE TABLE assets (
    id         UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(100) NOT NULL,
    type       VARCHAR(20)  NOT NULL CHECK (type IN ('RENDA_FIXA','RENDA_VARIAVEL','FII','ETF')),
    ticker     VARCHAR(10),
    notes      TEXT,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMPTZ
);

CREATE UNIQUE INDEX uq_assets_ticker ON assets (ticker) WHERE deleted_at IS NULL AND ticker IS NOT NULL;
CREATE INDEX idx_assets_type ON assets (type) WHERE deleted_at IS NULL;
