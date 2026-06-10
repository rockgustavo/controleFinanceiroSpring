package com.controleFinanceiro.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Asset {

    private UUID id;
    private String name;
    private AssetType type;
    private String ticker;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant archivedAt;

    private Asset() {}

    public static Asset create(String name, AssetType type, String ticker, String notes) {
        var asset = new Asset();
        asset.name = name;
        asset.type = type;
        asset.ticker = ticker;
        asset.notes = notes;
        asset.createdAt = Instant.now();
        asset.updatedAt = Instant.now();
        return asset;
    }

    public static Asset reconstitute(UUID id, String name, AssetType type, String ticker,
                                     String notes, Instant createdAt, Instant updatedAt, Instant archivedAt) {
        var asset = new Asset();
        asset.id = id;
        asset.name = name;
        asset.type = type;
        asset.ticker = ticker;
        asset.notes = notes;
        asset.createdAt = createdAt;
        asset.updatedAt = updatedAt;
        asset.archivedAt = archivedAt;
        return asset;
    }

    public void update(String name, String ticker, String notes) {
        if (name != null) this.name = name;
        if (ticker != null) this.ticker = ticker;
        if (notes != null) this.notes = notes;
        this.updatedAt = Instant.now();
    }

    public void archive() {
        this.archivedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public boolean isArchived() { return archivedAt != null; }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public AssetType getType() { return type; }
    public String getTicker() { return ticker; }
    public String getNotes() { return notes; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getArchivedAt() { return archivedAt; }
}
