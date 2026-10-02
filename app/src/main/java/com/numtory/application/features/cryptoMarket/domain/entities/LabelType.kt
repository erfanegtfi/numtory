package com.numtory.application.features.cryptoMarket.domain.entities

enum class LabelType(val raw: String, val display: String) {
    STOCK("stock", "سهام"),
    METALS("precious_metals_energy", "فلزات و انرژی"),
    Memes("Memes", "میم کوین"),
    AI("AI & Big Data", "هوش مصنوعی"),
    Web3("Web3", "وب 3"),
    NFT("Collectibles & NFTs", "NFT"),
    Gaming("Gaming", "بازی"),
    DeFi("DeFi", "غیر متمرکز");

    companion object {
        fun from(raw: String): LabelType? =
            entries.firstOrNull { it.raw.equals(raw, ignoreCase = true) }
    }
}