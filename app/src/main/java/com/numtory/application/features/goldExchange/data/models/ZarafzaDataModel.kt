package com.numtory.application.features.goldExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.goldExchange.domain.entities.Zarafza
import com.numtory.application.features.goldExchange.domain.entities.ZarafzaData
import com.numtory.application.features.goldExchange.domain.entities.ZarafzaGold
import com.numtory.application.features.goldExchange.domain.entities.ZarafzaPrice

data class ZarafzaDataModel(
    @SerializedName("data")
    val data: ZarafzaDataDataModel?,
) {
    fun toEntity(): Zarafza = Zarafza(data?.toEntity())
}

data class ZarafzaDataDataModel(
    @SerializedName("G18")
    val gold18: ZarafzaGoldDataModel?,
) {
    fun toEntity(): ZarafzaData = ZarafzaData(gold18?.toEntity())
}

data class ZarafzaGoldDataModel(
    @SerializedName("sell")
    val sell: ZarafzaPriceDataModel?,
    @SerializedName("buy")
    val buy: ZarafzaPriceDataModel?,
) {
    fun toEntity(): ZarafzaGold = ZarafzaGold(sell?.toEntity(), buy?.toEntity())
}

data class ZarafzaPriceDataModel(
    @SerializedName("price")
    val price: Double?,
) {
    fun toEntity(): ZarafzaPrice = ZarafzaPrice(price)
}
