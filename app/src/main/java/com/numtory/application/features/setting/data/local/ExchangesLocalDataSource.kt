package com.numtory.application.features.setting.data.local

import com.google.gson.Gson
import com.numtory.application.data.local.preferences.PreferencesConstants
import com.numtory.application.data.local.preferences.Session
import com.numtory.application.features.market.data.models.ExchangeInfoDataModel
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.serialization.json.Json

interface ExchangesLocalDataSource {

    fun saveAddFee(addFee: Boolean?)
    fun addFee(): Boolean
}

class ExchangesLocalDataSourceImpl constructor(
    private val session: Session,
) : ExchangesLocalDataSource {

    override fun saveAddFee(addFee: Boolean?) =
        session.setPreferenceValue(PreferencesConstants.ADD_FEE, addFee ?: false)

    override fun addFee(): Boolean =
        session.getPreferenceValue(PreferencesConstants.ADD_FEE, false)


}