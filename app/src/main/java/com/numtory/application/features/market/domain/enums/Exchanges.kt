package com.numtory.application.features.market.domain.enums

import com.numtory.application.R

enum class Exchanges(val title: String, val logo: Int) {
    none("", R.drawable.launcher),
    tetherland("تترلند", R.drawable.tetherland),
    abantether("آبان تتر", R.drawable.aban),
    bitpin("بیت پین", R.drawable.bitpin),
    nobitex("نوبیتکس (سریع)", R.drawable.nobitex,),
    nobitexMarket("نوبیتکس (بازار)", R.drawable.nobitex),
    bit24("بیت 24 (سریع)", R.drawable.bit24),
    bit24Market("بیت 24 (بازار)", R.drawable.bit24),
    arzplus("ارزپلاس (سریع)", R.drawable.arzplus),
    arzplusMarket("ارزپلاس (بازار)", R.drawable.arzplus),
    coinkade("کوین کده", R.drawable.coinkade),
    twox("توایکس", R.drawable.twox),
    tabdeal("تبدیل (سریع)", R.drawable.tabdeal),
    tabdealMarket("تبدیل (بازار)", R.drawable.tabdeal),
    pooleno("پول نو", R.drawable.pooleno),
    eterex("اتراکس", R.drawable.eterex),
    sarmayex("سرمایکس (سریع)", R.drawable.sarmayex),
    sarmayexMarket("سرمایکس (بازار)", R.drawable.sarmayex),
    pingi("پینگی", R.drawable.pingi,),
	wallex("والکس", R.drawable.wallex),
	saraf("صراف", R.drawable.saraf),
	arz3("ارز 3", R.drawable.arz3),
	ubitex("یوبیتکس", R.drawable.ubitex),
    ramzinex("رمزینکس (سریع)", R.drawable.ramzinex);


    companion object {
        fun fromString(value: String): Exchanges {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: none
        }

//        fun fromString(value: String): Exchanges? {
//            return enumValues<Exchanges>().find { it.name.equals(value, ignoreCase = true) }
//        }
    }
}