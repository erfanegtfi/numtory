package com.numtory.application.features.market.data.local

import com.google.gson.Gson
import com.numtory.application.data.local.preferences.PreferencesConstants
import com.numtory.application.data.local.preferences.Session
import com.numtory.application.features.market.data.models.ExchangeStatusDataModel
import com.numtory.application.features.market.domain.entities.ExchangeStatus
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.serialization.json.Json

interface ExchangesLocalDataSource {
     fun getExchangesStatus(): List<ExchangeStatus>?
     fun saveExchangesStatus(exchanges: List<ExchangeStatusDataModel>)

    fun saveDisplayExchanges(exchanges: List<Exchanges>?)
    fun getDisplayExchanges(): List<Exchanges>?

    fun saveAddFee(addFee: Boolean?)
    fun addFee(): Boolean
}

class ExchangesLocalDataSourceImpl constructor(
    private val session: Session,
) : ExchangesLocalDataSource {
    override  fun getExchangesStatus(): List<ExchangeStatus>? {
        val exchangeStatus = session.getArrayObject(
            PreferencesConstants.EXCHANGES_STATUS,
            Array<ExchangeStatusDataModel>::class.java
        )
        return exchangeStatus?.map {
            it.toEntity()
        }
    }

    override  fun saveExchangesStatus(exchanges: List<ExchangeStatusDataModel>) {
        val json = Gson().toJson(exchanges)
        session.setPreferenceValue(PreferencesConstants.EXCHANGES_STATUS, json)
    }


    override fun saveDisplayExchanges(exchanges: List<Exchanges>?) {
        val json = Json.encodeToString(exchanges)
        session.setPreferenceValue(PreferencesConstants.ENABLE_EXCHANGES, json)
    }

    override fun getDisplayExchanges(): List<Exchanges>? =
        session.getArrayObject(PreferencesConstants.ENABLE_EXCHANGES, Array<Exchanges>::class.java)

    override fun saveAddFee(addFee: Boolean?) =
        session.setPreferenceValue(PreferencesConstants.ADD_FEE, addFee ?: false)

    override fun addFee(): Boolean =
        session.getPreferenceValue(PreferencesConstants.ADD_FEE, false)


}