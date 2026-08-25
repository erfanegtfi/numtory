package com.numtory.application.features.cryptoMarket.data.local

import com.numtory.application.data.local.preferences.PreferencesConstants
import com.numtory.application.data.local.preferences.Session
import kotlinx.serialization.json.Json

interface CryptoGlobalExchangesLocalDataSource {

    fun saveUserCryptoGlobalMarketPrices(exchanges: List<String>?)
    fun getUserCryptoGlobalMarketPrices(): List<String>?

}

class CryptoGlobalExchangesLocalDataSourceImpl constructor(
    private val session: Session,
) : CryptoGlobalExchangesLocalDataSource {


    override fun saveUserCryptoGlobalMarketPrices(exchanges: List<String>?) {
        val json = Json.encodeToString(exchanges)
        session.setPreferenceValue(PreferencesConstants.GLOBAL_CRYPTO_SYMBOLS, json)
    }

    override fun getUserCryptoGlobalMarketPrices(): List<String>? =
        session.getArrayObject(PreferencesConstants.GLOBAL_CRYPTO_SYMBOLS, Array<String>::class.java)

}