package com.numtory.application.features.goldExchange.presenter.marketOptions

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eterex.composeui.ButtonComponent
import com.numtory.application.common.goldSettingScreenOpened
import com.numtory.application.common.showFeeCheckbox
import com.numtory.application.composeUI.MyCheckbox
import com.numtory.application.features.goldExchange.domain.entities.GoldExchangeInfo
import com.numtory.application.features.goldExchange.domain.enums.GoldExchanges
import com.numtory.application.features.goldExchange.presenter.marketOptions.components.BottomSheetAppbar
import com.numtory.application.features.goldExchange.presenter.marketOptions.components.ExchangesListWithSelectAll
import com.numtory.application.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoldAssetOptionsBottomSheetScreen(
    userExchanges: List<GoldExchanges>,
    allExchanges: List<GoldExchangeInfo>,
    addFee: Boolean,
    hide: () -> Unit,
    onSubmit: (exchanges: Map<GoldExchanges, Boolean>, addFee: Boolean) -> Unit
) {

//    val allExchanges = Exchanges.entries
    val checkedStates = remember { mutableStateMapOf<GoldExchanges, Boolean>() }

    // Initialize
    LaunchedEffect(Unit) {
        allExchanges.forEach { info ->
            checkedStates[info.exchange] = userExchanges.contains(info.exchange) == true
        }
    }
    LaunchedEffect(Unit) {
        goldSettingScreenOpened()
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
    allExchanges: List<GoldExchangeInfo>,
    checkedStates: MutableMap<GoldExchanges, Boolean>, check: Boolean
) {
    allExchanges.forEach { info ->
        if (info.isMarket == true)
            checkedStates[info.exchange] = check
        else checkedStates[info.exchange] = false
    }
}

fun setAll(
    allExchanges: List<GoldExchangeInfo>,
    checkedStates: MutableMap<GoldExchanges, Boolean>, check: Boolean
) {
    allExchanges.forEach { info ->
        checkedStates[info.exchange] = check
    }
}


@Preview
@Composable
fun AssetOptionsBottomSheetScreenPreview() {
    MyApplicationTheme {
//        AssetOptionsBottomSheetScreen(emptyList(), false, hide = {}, onSubmit = { a, b -> })
    }
}