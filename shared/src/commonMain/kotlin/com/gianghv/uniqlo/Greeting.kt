package com.gianghv.uniqlo

import com.gianghv.uniqlo.platform.Platform
import com.gianghv.uniqlo.utils.CFlow
import com.gianghv.uniqlo.utils.wrap
import com.mirego.konnectivity.Konnectivity
import com.mirego.konnectivity.NetworkState
import kotlinx.coroutines.flow.map

class Greeting {
    private val platform = Platform()
    private val konnectivity = Konnectivity()

    private val greetingText = buildString {
        appendLine("Hello! 👋")
        appendLine(platform.system)
        appendLine(platform.locale)
        appendLine(platform.version)
        appendLine()
    }

    fun greeting(): CFlow<String> = konnectivity.networkState
        .map { networkState ->
            greetingText + networkState.asGreetingInfo()
        }
        .wrap()

    private fun NetworkState.asGreetingInfo(): String = "By the way, you're " + when (this) {
        NetworkState.Unreachable -> "offline. 🔌"
        is NetworkState.Reachable -> when (metered) {
            true -> "online, but your connection is metered. 📶"
            else -> "online! 🛜"
        }
    }
}
