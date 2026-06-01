package com.numtory.application.features.market.domain.entities


data class MarketPrice constructor(
    var symbol: String? = null,
    var sellPrice: String? = null,
    var buyPrice: String? = null,
    var marketPrice: String? = null,
    var exchangeInfo: ExchangeInfo,
    var lastRefresh: Long?,
    var addFee: Boolean? = null

) {

//    val finalSellPrice: String
//        get(): String {
//
//            // no fee for market
//            if (marketPrice != null && marketPrice?.toFloat() != 0f)
//                return marketPrice.toString()
//
//            val p = if (sellPrice == null || sellPrice?.toFloat() == 0f)
//                0f
//            else
//                sellPrice?.toFloat() ?: 0f
//
//
//            return if (addFee == true) (p - (p * (exchange?.fee
//                ?: 0f))).toString() else p.toString()
//        }

//    val finalBuyPrice: String
//        get(): String {
//            // no fee for market
//            if (marketPrice != null && marketPrice?.toFloat() != 0f)
//                return marketPrice.toString()
//
//            val p: Float = if (buyPrice == null || buyPrice?.toFloat() == 0f)
//                0f
//            else
//                buyPrice?.toFloat() ?: 0f
//
//
//            return if (addFee == true) (p + (p * (exchange?.fee
//                ?: 0f))).toString() else p.toString()
//        }

    val finalSellPrice: String
        get(): String {

            val p = if (marketPrice != null && marketPrice?.toDouble() != 0.0) {
                marketPrice?.toDouble() ?: 0.0
            } else {
                if (sellPrice == null || sellPrice?.toDouble() == 0.0)
                    0.0
                else
                    sellPrice?.toDouble() ?: 0.0
            }


            return if (addFee == true) (p - (p * (exchangeInfo.fee
                ?: 0f))).toString() else p.toString()
        }

    val finalBuyPrice: String
        get(): String {
            val p = if (marketPrice != null && marketPrice?.toDouble() != 0.0) {
                marketPrice?.toDouble() ?: 0.0
            } else {
                if (buyPrice == null || buyPrice?.toDouble() == 0.0)
                    0.0
                else
                    buyPrice?.toDouble() ?: 0.0
            }


            return if (addFee == true) (p + (p * (exchangeInfo.fee
                ?: 0f))).toString() else p.toString()
        }
    val diff: String
        get(): String {
            return (finalBuyPrice.toDouble() - finalSellPrice.toDouble()).toLong().toString()
        }
}