package com.numtory.application.features.seke.domain.entities

import com.numtory.application.common.stringToMillisSimple


data class SekePrice constructor(
    var title: String?,
    var symbol: String?,
    var sell: String?,
    var lastUpdate: String?,
) {

    val lastUpdateSec: Long?
        get() = stringToMillisSimple(lastUpdate)


}
