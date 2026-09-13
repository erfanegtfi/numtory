package com.numtory.application.features.goldExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.goldExchange.domain.entities.HamrahGold
import com.numtory.application.features.goldExchange.domain.entities.HamrahGoldData

//data class HamrahGoldDataModel(
//    @SerializedName("buy")
//    val buy: List<Long>?,
//    @SerializedName("sell")
//    val sell: List<Long>?,
//) {
//    fun toEntity(): HamrahGold = HamrahGold(buy, sell)
//}

data class HamrahGoldDataModel(
    @SerializedName("data")
    val data: HamrahGoldDataDataModel,

) {
    fun toEntity(): HamrahGold = HamrahGold(data.toEntity())
}

data class HamrahGoldDataDataModel(
    @SerializedName("current")
    val current: Long?,

) {
    fun toEntity(): HamrahGoldData = HamrahGoldData(current)
}
