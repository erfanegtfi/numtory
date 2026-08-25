package com.numtory.application.features.goldExchange.data.local

import com.google.gson.Gson
import com.numtory.application.data.local.preferences.PreferencesConstants
import com.numtory.application.data.local.preferences.Session
import com.numtory.application.features.goldExchange.data.models.GoldExchangeInfoDataModel
import com.numtory.application.features.goldExchange.domain.entities.GoldExchangeInfo
import com.numtory.application.features.goldExchange.domain.enums.GoldExchanges
import kotlinx.serialization.json.Json

interface GoldExchangesLocalDataSource {
     fun getGoldExchangesInfo(): List<GoldExchangeInfo>?
     fun saveGoldExchangesInfo(exchanges: List<GoldExchangeInfoDataModel>)

    fun saveUserGoldExchanges(exchanges: List<GoldExchanges>?)
    fun getUserGoldExchanges(): List<GoldExchanges>?

    fun saveAddFee(addFee: Boolean?)
    fun addFee(): Boolean
}

class GoldExchangesLocalDataSourceImpl constructor(
    private val session: Session,
) : GoldExchangesLocalDataSource {
    override  fun getGoldExchangesInfo(): List<GoldExchangeInfo>? {
        val exchangeStatus = session.getArrayObject(
            PreferencesConstants.GOLD_EXCHANGES_STATUS,
            Array<GoldExchangeInfoDataModel>::class.java
        )
        return exchangeStatus?.map {
            it.toEntity()
        }
    }

    override  fun saveGoldExchangesInfo(exchanges: List<GoldExchangeInfoDataModel>) {
        val json = Gson().toJson(exchanges)
        session.setPreferenceValue(PreferencesConstants.GOLD_EXCHANGES_STATUS, json)
    }


    override fun saveUserGoldExchanges(exchanges: List<GoldExchanges>?) {
        val json = Json.encodeToString(exchanges)
        session.setPreferenceValue(PreferencesConstants.ENABLE_GOLD_EXCHANGES, json)
    }

    override fun getUserGoldExchanges(): List<GoldExchanges>? =
        session.getArrayObject(PreferencesConstants.ENABLE_GOLD_EXCHANGES, Array<GoldExchanges>::class.java)

    override fun saveAddFee(addFee: Boolean?) =
        session.setPreferenceValue(PreferencesConstants.ADD_GOLD_FEE, addFee ?: false)

    override fun addFee(): Boolean =
        session.getPreferenceValue(PreferencesConstants.ADD_GOLD_FEE, false)


}