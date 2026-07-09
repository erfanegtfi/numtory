package com.numtory.application.features.gold.domain.enums

import com.numtory.application.R

enum class GoldExchanges(val title: String, val logo: Int) {
    none("", R.drawable.image_placeholder),
    digikala("دیجی کالا", R.drawable.digigold),
    hamrahgold("همراه گلد", R.drawable.hamrahgold),
    taline("طلاین", R.drawable.taline),
    melligold("ملی گلد", R.drawable.melligold),
    talasea("طلاسی", R.drawable.talasea),
    milli("میلی", R.drawable.milli),
    technoGold("تکنوگلد", R.drawable.technogold),
    wallgold("وال گلد", R.drawable.wallgold),
    daric("داریک", R.drawable.daric),
    ecogold("اکو گلد", R.drawable.ecogold),
    zarminex("زرمینکس", R.drawable.zarminex),
    noghresea("نقره سی", R.drawable.noghresea),
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