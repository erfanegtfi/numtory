package com.numtory.application.features.cryptoMarket.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.cryptoMarket.domain.entities.EterexAssetLabel


//data class CryptoMarketItemDataModel constructor(
//    @SerializedName("id")
//    var id: String?,
//    @SerializedName("current_price")
//    var price: String?,
//    @SerializedName("name")
//    var name: String?,
//    @SerializedName("symbol")
//    var symbol: String?,
//    @SerializedName("image")
//    var image: String?,
//    @SerializedName("price_change_percentage_24h")
//    var dayChangePercent: Float?,
//    @SerializedName("price_change_24h")
//    var dayChangePrice: Float?
//) {
//
//    fun toEntity(): CryptoMarketPrice =
//        CryptoMarketPrice(
//            id,
//            price,
//            symbol,
//            name,
//            image,
//            dayChangePercent ?: 0f,
//            dayChangePrice ?: 0f
//        )
//}

data class EterexAssetLabelResponse constructor(
    @SerializedName("coins")
    var coins: List<EterexAssetLabelDataModel>?,

    ) {

    fun toEntity(): List<EterexAssetLabel>? =
        coins?.map { it.toEntity() }
}

data class EterexAssetLabelDataModel constructor(
    @SerializedName("id")
    var id: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("persian_name")
    var persianName: String?,
    @SerializedName("full_name")
    var fullName: String?,
    @SerializedName("labels")
    var labels: List<List<String>>?,

    ) {

    fun toEntity(): EterexAssetLabel =
        EterexAssetLabel(id, name, persianName, fullName, labels)
}
