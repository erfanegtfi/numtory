package com.numtory.application.features.notification.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Carries a route from a notification tap (handled in MainActivity) to whichever screen can act on
 * it. MainScreen owns the bottom-tab NavHost, so it collects this and navigates once the graph is
 * composed — the tap can otherwise arrive before there is anything to navigate.
 */
class DeepLinkRouter {

    private val _route = MutableStateFlow<String?>(null)
    val route: StateFlow<String?> = _route.asStateFlow()

    fun push(route: String?) {
        if (!route.isNullOrBlank()) _route.value = route
    }

    fun consume() {
        _route.value = null
    }
}
