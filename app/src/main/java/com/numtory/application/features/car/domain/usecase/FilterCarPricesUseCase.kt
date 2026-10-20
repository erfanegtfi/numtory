package com.numtory.application.features.car.domain.usecase

import com.numtory.application.features.car.domain.entities.CarCategory


class FilterCarPricesUseCase {

    operator fun invoke(
        prices: List<CarCategory>,
        searchQuery: String? = null,
    ): List<CarCategory> {
        val query = searchQuery?.trim()?.lowercase()?.takeIf { it.isNotEmpty() }
            ?: return prices

        return prices.mapNotNull { category ->
            if (matchesQuery(category, query)) {
                // Category name matches → keep whole category
                category
            } else {
                // Otherwise keep only matching cars within the category
                val matchingCars = category.cars?.filter { car ->
                    car.name?.lowercase()?.contains(query) == true
                }
                if (!matchingCars.isNullOrEmpty()) {
                    category.copy(cars = matchingCars)
                } else {
                    null
                }
            }
        }
    }

    private fun matchesQuery(category: CarCategory, query: String): Boolean {
        if (category.name?.lowercase()?.contains(query) == true) return true
//        return category.cars?.any { car ->
//            car.name?.lowercase()?.contains(query) == true
//        } == true

        return false
    }
}