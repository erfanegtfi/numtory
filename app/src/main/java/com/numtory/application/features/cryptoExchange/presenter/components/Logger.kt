package com.numtory.application.features.cryptoExchange.presenter.components

import android.util.Log
import com.numtory.application.common.priceFormatter
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import kotlin.text.toDouble

fun printLogs(priceList: ViewState<List<MarketPrice>>) {


    val col1Width = 10
    val col2Width = 10
    val col3Width = 16

    val sb = StringBuilder()

    sb.append("\n")

// Rows
    var averageBuyPrice = 0f
    var averageSellPrice = 0f
    if (priceList is ViewState.Success) {

        priceList.data.forEach { item ->
            averageBuyPrice += (item.finalBuyPrice).toDouble().toLong()
            averageSellPrice += (item.finalSellPrice).toDouble().toLong()

        }

        sb.appendLine(
            "میانگین خرید: " + priceFormatter(
                (averageBuyPrice / priceList.data.size).toDouble().toLong().toString()
            ),
        )
        sb.appendLine(
            "میانگین فروش: " + priceFormatter(
                (averageSellPrice / priceList.data.size).toDouble().toLong().toString()
            ),
        )

    }
    // Header
    sb.append("\n")
    sb.append(
        String.format(
            "%-${col1Width}s %${col2Width}s  %${col3Width}s \n",
            "فروش", "خرید", "صرافی"
        )
    )

// Separator line (optional but nice)
//    sb.append("-".repeat(col1Width + col2Width +  col3Width + 10))
//    sb.append("\n")
    if (priceList is ViewState.Success) {

        priceList.data.forEach { item ->
            sb.append(
                String.format(
                    "%-${col1Width}s %${col2Width}s  %${col3Width}s\n",
//                    priceFormatter(
//                        ((item.data.buyPrice?.toFloat() ?: 0f) - (item.data.sellPrice?.toFloat()
//                            ?: 0f)).toString(),
//                    ),
                    priceFormatter(item.finalSellPrice.toDouble().toLong().toString()),
                    priceFormatter(item.finalBuyPrice.toDouble().toLong().toString()),
                    item.exchangeInfo.exchange.title,
                )
            )
        }
    }

// Print to Logcat
    Log.d("PRICE_TABLE", sb.toString())

}
