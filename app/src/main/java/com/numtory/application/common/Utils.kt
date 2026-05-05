package com.numtory.application.common

import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.let
import kotlin.math.log10
import kotlin.math.pow
import kotlin.ranges.coerceAtLeast
import kotlin.text.format
import kotlin.text.lowercase
import kotlin.text.toDouble

val Double.formatHumanReadable: String
    get() = log10(coerceAtLeast(1.0)).toInt().div(3).let {
        val precision = when (it) {
            0 -> 0; else -> 1
        }
        val suffix = arrayOf("", "K", "M", "G", "T", "P", "E", "Z", "Y")
        String.format("%.${precision}f ${suffix[it]}", toDouble() / 10.0.pow(it * 3))
    }


fun priceFormatter(myNumber: String): String {
    val formatter: NumberFormat = DecimalFormat("#,###.######")
    return formatter.format(myNumber.toDouble())
}

fun getFullImageURL(asset: String): String {
    return "https://static-dl.eterex.com/icons/png/${asset.lowercase()}_.png"
}

fun formatDuration(totalSeconds: Long?): String {
    if (totalSeconds == null) return ""

    val minutes = totalSeconds / 60
    val hours = minutes / 60
    val days = hours / 24
    return when {
        days > 0 -> "${days} روز"
        hours > 0 -> "${hours} ساعت"
        minutes > 0 -> "${minutes} دقیقه"
        else -> "کمتر از یک دقیقه"
    }
}