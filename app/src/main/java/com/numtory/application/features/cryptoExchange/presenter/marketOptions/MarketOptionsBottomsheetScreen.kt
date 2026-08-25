package com.numtory.application.features.cryptoExchange.presenter.marketOptions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eterex.composeui.ButtonComponent
import com.numtory.application.common.onlyShowMarketsCheckbox
import com.numtory.application.common.exchangesSettingScreenOpened
import com.numtory.application.common.showFeeCheckbox
import com.numtory.application.composeUI.BottomSheetAppbar
import com.numtory.application.composeUI.MyCheckbox
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetOptionsBottomSheetScreen(
    userExchanges: List<Exchanges>,
    allExchanges: List<ExchangeInfo>,
    addFee: Boolean,
    hide: () -> Unit,
    onSubmit: (exchanges: Map<Exchanges, Boolean>, addFee: Boolean) -> Unit
) {

//    val allExchanges = Exchanges.entries
    val checkedStates = remember { mutableStateMapOf<Exchanges, Boolean>() }

    // Initialize
    LaunchedEffect(Unit) {
        allExchanges.forEach { info ->
            checkedStates[info.exchange] = userExchanges.contains(info.exchange) == true
        }
    }
    LaunchedEffect(Unit) {
        exchangesSettingScreenOpened()
    }

    val allChecked = remember { mutableStateOf(false) }
    val addFeeChecked = remember { mutableStateOf(addFee) }
    val onlyMarketsChecked = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        BottomSheetAppbar(hide, title = "فیلترها")
        Box(modifier = Modifier.height(16.dp))
        MyCheckbox("اعمال کارمزد در قیمت ها", addFeeChecked.value) {
            addFeeChecked.value = it
            showFeeCheckbox()
        }
//        Text(
//            "کارمزد روی قیمت بازار اعمال نمی شود",
//            modifier = Modifier
//                .align(Alignment.Start)
//                .padding(start = 50.dp),
//            style = MaterialTheme.typography.labelMedium
//        )
//        Box(modifier = Modifier.height(6.dp))

        MyCheckbox("فقط نمایش بازارها", onlyMarketsChecked.value) {
            onlyMarketsChecked.value = it
            setMarkets(allExchanges, checkedStates, it)

            if (allChecked.value)
                setAll(allExchanges, checkedStates, true)

            onlyShowMarketsCheckbox()
        }
//        Text(
//            "صرافی هایی که قیمت بازار دارند و صرافی هایی که فقط تبدیل سریع دارند نمایش داده شوند",
//            modifier = Modifier
//                .align(Alignment.Start)
//                .padding(start = 50.dp),
//            style = MaterialTheme.typography.labelMedium
//        )
//        Box(modifier = Modifier.height(6.dp))
        MyCheckbox("نمایش همه", allChecked.value) {
            allChecked.value = it
            setAll(allExchanges, checkedStates, it)

            if (onlyMarketsChecked.value)
                setMarkets(allExchanges, checkedStates, true)
        }
        ExchangesListWithSelectAll(
            Modifier
                .fillMaxWidth()
                .weight(1f), allExchanges, checkedStates
        ) {}
        Box(modifier = Modifier.height(16.dp))

        ButtonComponent(
            "ذخیره",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
            onButtonClicked = {
                onSubmit(checkedStates, addFeeChecked.value)
            },
        )
    }
}

fun setMarkets(
    allExchanges: List<ExchangeInfo>,
    checkedStates: MutableMap<Exchanges, Boolean>, check: Boolean
) {
    allExchanges.forEach { info ->
        if (info.isMarket == true)
            checkedStates[info.exchange] = check
        else checkedStates[info.exchange] = false
    }
}

fun setAll(
    allExchanges: List<ExchangeInfo>,
    checkedStates: MutableMap<Exchanges, Boolean>, check: Boolean
) {
    allExchanges.forEach { info ->
        checkedStates[info.exchange] = check
    }
}