package com.numtory.application.features.market.data.local

import com.numtory.application.data.local.preferences.PreferencesConstants
import com.numtory.application.data.local.preferences.Session
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalDataRepositoryImp constructor(
    private val session: Session
) : LocalDataRepository {


    override fun saveEnableExchanges(exchanges: List<Exchanges>?) {
        val json = Json.encodeToString(exchanges)
        session.setPreferenceValue(PreferencesConstants.ENABLE_EXCHANGES, json)
    }

    override fun getEnableExchanges(): List<Exchanges>? =
        session.getArrayObject(PreferencesConstants.ENABLE_EXCHANGES, Array<Exchanges>::class.java)

    override fun saveAddFee(addFee: Boolean?) =
        session.setPreferenceValue(PreferencesConstants.ADD_FEE, addFee ?: false)

    override fun addFee(): Boolean =
        session.getPreferenceValue(PreferencesConstants.ADD_FEE, false)


}