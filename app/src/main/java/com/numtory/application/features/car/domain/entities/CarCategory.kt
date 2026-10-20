package com.numtory.application.features.car.domain.entities

data class CarCategory(
    val name: String?,
    val cars: List<Car>?
)

data class Car(
    val name: String?,
    val price: Long?,
    val change: Long?,
    val trend: Trend?
)

enum class Trend(val value: String) {
    UP("up"),
    DOWN("down");

    companion object {
        fun from(value: String?): Trend? =
            entries.firstOrNull { it.value.equals(value, ignoreCase = true) }
    }
}