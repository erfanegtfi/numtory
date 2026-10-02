package com.numtory.application.features.cryptoMarket.domain.usecase

import android.util.Log
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.cryptoMarket.domain.entities.LabelType

class FilterCryptoPricesUseCase {

    operator fun invoke(
        prices: List<CryptoMarketPrice>,
        searchQuery: String? = null,
        label: LabelType? = null
    ): List<CryptoMarketPrice> {
        val query = searchQuery?.trim()?.lowercase()

        return prices.filter { asset ->
            matchesQuery(asset, query) && matchesLabel(asset, label)
        }
    }

    private fun matchesQuery(asset: CryptoMarketPrice, query: String?): Boolean {
        if (query.isNullOrEmpty()) return true
        return asset.symbol?.lowercase()?.contains(query) == true ||
                asset.name?.lowercase()?.contains(query) == true
    }

    private fun matchesLabel(asset: CryptoMarketPrice, label: LabelType?): Boolean {
        if (label == null) return true

        var hasLabel = false

        asset.labels?.forEach { la ->
            if (!hasLabel)
                hasLabel = LabelType.entries.toTypedArray().any {
                    la.contains(label.raw)
                }
        }

        return hasLabel
    }
}