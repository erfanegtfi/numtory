package com.numtory.application.features.market.data.models

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.numtory.application.features.market.domain.entities.Asacoine
import com.numtory.application.features.market.domain.entities.AsacoinePair


data class AsacoineDataModel constructor(
    @SerializedName("data")
    var data: AsacoinePairsDataModel?,
) {

    fun toEntity(): Asacoine =
        Asacoine(data?.toEntity())
}

/**
 * Asacoine answers in a columnar shape: [keys] names the columns once and every
 * row in [values] carries them positionally, so each field is read by its key index
 * rather than by name.
 */
data class AsacoinePairsDataModel constructor(
    @SerializedName("keys")
    var keys: List<String>?,
    @SerializedName("values")
    var values: List<List<JsonElement>>?,
) {

    fun toEntity(): List<AsacoinePair>? {
        val keys = keys ?: return null
        val nameIndex = keys.indexOf(KEY_NAME)
        val marketIndex = keys.indexOf(KEY_MARKET)
        if (nameIndex < 0 || marketIndex < 0) return null

        return values?.map { row ->
            val market = row.getOrNull(marketIndex)
                ?.takeIf { it.isJsonObject }
                ?.asJsonObject

            AsacoinePair(
                name = row.getOrNull(nameIndex).asStringOrNull(),
                ask = market?.get(KEY_ASK).asStringOrNull(),
                bid = market?.get(KEY_BID).asStringOrNull(),
            )
        }
    }

    private fun JsonElement?.asStringOrNull(): String? =
        this?.takeIf { it.isJsonPrimitive }?.asString

    private companion object {
        const val KEY_NAME = "name"
        const val KEY_MARKET = "market"
        const val KEY_ASK = "ask"
        const val KEY_BID = "bid"
    }
}
