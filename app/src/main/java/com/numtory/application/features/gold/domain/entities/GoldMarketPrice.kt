package com.numtory.application.features.gold.domain.entities

data class GoldMarketPrice constructor(
    var sellPrice: String? = null,
    var buyPrice: String? = null,
    var marketPrice: String? = null,
    var exchangeInfo: GoldExchangeInfo,
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

            val p = if (marketPrice != null && marketPrice?.toFloat() != 0f) {
                marketPrice?.toFloat() ?: 0f
            } else {
                if (sellPrice == null || sellPrice?.toFloat() == 0f)
                    0f
                else
                    sellPrice?.toFloat() ?: 0f
            }


            return if (addFee == true) (p - (p * (exchangeInfo.fee
                ?: 0f))).toString() else p.toString()
        }

    val finalBuyPrice: String
        get(): String {
            val p = if (marketPrice != null && marketPrice?.toFloat() != 0f) {
                marketPrice?.toFloat() ?: 0f
            } else {
                if (buyPrice == null || buyPrice?.toFloat() == 0f)
                    0f
                else
                    buyPrice?.toFloat() ?: 0f
            }


            return if (addFee == true) (p + (p * (exchangeInfo.fee
                ?: 0f))).toString() else p.toString()
        }
    val diff: String
        get(): String {
            return (finalBuyPrice.toFloat() - finalSellPrice.toFloat()).toInt().toString()
        }
}