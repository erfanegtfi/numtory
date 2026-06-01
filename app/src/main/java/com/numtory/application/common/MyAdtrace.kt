package com.numtory.application.common

import io.adtrace.sdk.AdTrace
import io.adtrace.sdk.AdTraceEvent


fun appOpened(){
//    val adtraceEvent = AdTraceEvent("3j3bm7")
//    AdTrace.trackEvent(adtraceEvent)
}

fun goldExchangesScreenOpened(){
//    val adtraceEvent = AdTraceEvent("dej992")
//    AdTrace.trackEvent(adtraceEvent)
}

fun cryptoExchangesScreenOpened(){
//    val adtraceEvent = AdTraceEvent("qbf65n")
//    AdTrace.trackEvent(adtraceEvent)
}

fun globalCryptoMarketExchangesScreenOpened(){
//    val adtraceEvent = AdTraceEvent("qbf65n")
//    AdTrace.trackEvent(adtraceEvent)
}

fun exchangeScannerScreenOpened(){
    val adtraceEvent = AdTraceEvent("v2bjad")
    AdTrace.trackEvent(adtraceEvent)
}

fun exchangesSettingScreenOpened(){
    val adtraceEvent = AdTraceEvent("37lsoa")
    AdTrace.trackEvent(adtraceEvent)
}

fun goldSettingScreenOpened(){
    val adtraceEvent = AdTraceEvent("c30gg5")
    AdTrace.trackEvent(adtraceEvent)
}

fun exchangesTokenListScreenOpened(symbol: String){
    val adtraceEvent = AdTraceEvent("c30gg5")
    adtraceEvent.addEventParameter("symbol", symbol)
    AdTrace.trackEvent(adtraceEvent)
}

fun aboutScreenOpened(){
    val adtraceEvent = AdTraceEvent("4dloof")
    AdTrace.trackEvent(adtraceEvent)
}

fun showFeeCheckbox(){
    val adtraceEvent = AdTraceEvent("7smtij")
    AdTrace.trackEvent(adtraceEvent)
}

fun onlyShowMarketsCheckbox(){
    val adtraceEvent = AdTraceEvent("t7yr77")
    AdTrace.trackEvent(adtraceEvent)
}