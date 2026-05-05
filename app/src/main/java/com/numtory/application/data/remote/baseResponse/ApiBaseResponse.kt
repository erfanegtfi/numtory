package com.numtory.application.data.remote.baseResponse

import com.google.gson.annotations.SerializedName


open class ApiBaseResponse(
    @SerializedName("message") var message: Object? = null,
    @SerializedName("show_type") var showType: String? = null,
    @SerializedName("http_code") var httpCode: Int? = null,
    @SerializedName("success") var success: Boolean? = null,
)
