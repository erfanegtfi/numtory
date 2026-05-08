package com.numtory.application.features.market.data.local

import com.google.gson.Gson
import com.numtory.application.data.local.preferences.PreferencesConstants
import com.numtory.application.data.local.preferences.Session
import com.numtory.application.features.market.data.models.ExchangeStatusDataModel
import com.numtory.application.features.market.domain.entities.ExchangeStatus

interface ExchangesLocalDataSource {
     fun getExchanges(): List<ExchangeStatus>?
     fun saveExchanges(exchanges: List<ExchangeStatusDataModel>)

}

class ExchangesLocalDataSourceImpl constructor(
    private val session: Session,
) : ExchangesLocalDataSource {
    override  fun getExchanges(): List<ExchangeStatus>? {
        val exchangeStatus = session.getArrayObject(
            PreferencesConstants.EXCHANGES_STATUS,
            Array<ExchangeStatusDataModel>::class.java
        )
        return exchangeStatus?.map {
            it.toEntity()
        }
    }

    override  fun saveExchanges(exchanges: List<ExchangeStatusDataModel>) {
        val json = Gson().toJson(exchanges)
        session.setPreferenceValue(PreferencesConstants.EXCHANGES_STATUS, json)
    }


}