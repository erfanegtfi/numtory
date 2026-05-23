package com.numtory.application.features.gold.domain.enums

import com.numtory.application.R

enum class GoldExchanges(val title: String, val logo: Int) {
    none("", R.drawable.launcher),
    digikala("دیجی کالا", R.drawable.digigold),
    hamrahgold("همراه گلد", R.drawable.hamrahgold),
    taline("طلاین", R.drawable.taline),
    melligold("ملی گلد", R.drawable.melligold),
    goldika("گلدیکا", R.drawable.goldika);


    companion object {
        fun fromString(value: String): GoldExchanges {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: none
        }

//        fun fromString(value: String): Exchanges? {
//            return enumValues<Exchanges>().find { it.name.equals(value, ignoreCase = true) }
//        }
    }
}