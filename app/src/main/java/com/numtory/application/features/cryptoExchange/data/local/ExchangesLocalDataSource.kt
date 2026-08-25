package com.numtory.application.features.cryptoExchange.data.local

import com.google.gson.Gson
import com.numtory.application.data.local.preferences.PreferencesConstants
import com.numtory.application.data.local.preferences.Session
import com.numtory.application.features.cryptoExchange.data.models.ExchangeInfoDataModel
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges
import kotlinx.serialization.json.Json

interface ExchangesLocalDataSource {
     fun getExchangesInfo(): List<ExchangeInfo>?
     fun saveExchangesInfo(exchanges: List<ExchangeInfoDataModel>)

    fun saveUserExchanges(exchanges: List<Exchanges>?)
    fun getUserExchanges(): List<Exchanges>?

    fun saveAddFee(addFee: Boolean?)
    fun addFee(): Boolean
}

class ExchangesLocalDataSourceImpl constructor(
    private val session: Session,
) : ExchangesLocalDataSource {
    override  fun getExchangesInfo(): List<ExchangeInfo>? {
        val exchangeStatus = session.getArrayObject(
            PreferencesConstants.EXCHANGES_STATUS,
            Array<ExchangeInfoDataModel>::class.java
        )
        return exchangeStatus?.map {
            it.toEntity()
        }
    }

    override  fun saveExchangesInfo(exchanges: List<ExchangeInfoDataModel>) {
        val json = Gson().toJson(exchanges)
        session.setPreferenceValue(PreferencesConstants.EXCHANGES_STATUS, json)
    }


    override fun saveUserExchanges(exchanges: List<Exchanges>?) {
        val json = Json.encodeToString(exchanges)
        session.setPreferenceValue(PreferencesConstants.ENABLE_EXCHANGES, json)
    }

    override fun getUserExchanges(): List<Exchanges>? =
        session.getArrayObject(PreferencesConstants.ENABLE_EXCHANGES, Array<Exchanges>::class.java)

    override fun saveAddFee(addFee: Boolean?) =
        session.setPreferenceValue(PreferencesConstants.ADD_FEE, addFee ?: false)

    override fun addFee(): Boolean =
        session.getPreferenceValue(PreferencesConstants.ADD_FEE, false)


}