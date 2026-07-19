package com.numtory.application.features.notification.domain.entities

data class PushMessage(
    val title: String,
    val body: String,
    val route: String?
)
