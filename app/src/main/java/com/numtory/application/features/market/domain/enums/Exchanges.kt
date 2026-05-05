package com.numtory.application.features.market.domain.enums

import com.numtory.application.R

enum class Exchanges(val title: String, val logo: Int,val isMarket: Boolean, val bothTypes: Boolean, val fee: Float) {
    tetherland("تترلند", R.drawable.tetherland,false,false, 0.017f),
    abantether("آبان تتر", R.drawable.aban, false,false,0.003f),
    bitpin("بیت پین", R.drawable.bitpin, false,false,0.0035f),
    nobitex("نوبیتکس (سریع)", R.drawable.nobitex,false, true,0f),
    nobitexMarket("نوبیتکس (بازار)", R.drawable.nobitex, true,true,0.0025f),
    bit24("بیت 24 (سریع)", R.drawable.bit24,false, true,0.0f),
    bit24Market("بیت 24 (بازار)", R.drawable.bit24,true, true,0.002f),
    arzplus("ارزپلاس", R.drawable.arzplus,false,true,0.002f),
    arzplusMarket("ارزپلاس (بازار)", R.drawable.arzplus, true,true,0.002f),
    coinkade("کوین کده", R.drawable.coinkade, false,false,0.0035f),
    twox("توایکس", R.drawable.twox, false,false,0f),
    tabdeal("تبدیل (سریع)", R.drawable.tabdeal, false,true,0f),
    tabdealMarket("تبدیل (بازار)", R.drawable.tabdeal, true,true,0.0035f),
    pooleno("پول نو", R.drawable.pooleno, false,false,0f),
    eterex("اتراکس", R.drawable.eterex, false,false,0f),
    sarmayex("سرمایکس (سریع)", R.drawable.sarmayex,false, true,0f),
    sarmayexMarket("سرمایکس (بازار)", R.drawable.sarmayex,true, true,0.0034f),
    pingi("پینگی", R.drawable.pingi, false,false,0.0035f),
	wallex("والکس", R.drawable.wallex,false, false,0.003f),
}