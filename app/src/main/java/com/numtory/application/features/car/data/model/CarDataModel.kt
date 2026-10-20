package com.numtory.application.features.car.data.model


import com.google.gson.annotations.SerializedName
import com.numtory.application.features.car.domain.entities.Car
import com.numtory.application.features.car.domain.entities.CarCategory
import com.numtory.application.features.car.domain.entities.Trend
import kotlinx.serialization.Serializable

@Serializable
data class CarCategoryResponse(
    @SerializedName("categories") val cars: List<CarCategoryDataModel>?
){

    fun toEntity(): List<CarCategory>? = cars?.map { it.toEntity() }
}

@Serializable
data class CarCategoryDataModel(
    @SerializedName("name") val name: String?,
    @SerializedName("cars") val cars: List<CarDataModel>?
){

    fun toEntity(): CarCategory = CarCategory(
        name = name,
        cars = cars?.map { it.toEntity() }
    )
}

@Serializable
data class CarDataModel(
    @SerializedName("name") val name: String?,
    @SerializedName("price") val price: Long?,
    @SerializedName("change") val change: Long?,
    @SerializedName("trend") val trend: String?
){
    fun toEntity(): Car = Car(
        name = name ,
        price = price,
        change = change,
        trend = Trend.from(trend)
    )
}