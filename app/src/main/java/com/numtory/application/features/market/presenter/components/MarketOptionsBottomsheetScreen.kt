package com.numtory.application.features.market.presenter.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eterex.composeui.ButtonComponent
import com.numtory.application.R
import com.numtory.application.composeUI.MyCheckbox
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.enums.Exchanges
import com.numtory.application.ui.theme.MyApplicationTheme

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
        }
//        Text(
//            "کارمزد روی قیمت بازار اعمال نمی شود",
//            modifier = Modifier
//                .align(Alignment.Start)
//                .padding(start = 50.dp),
//            style = MaterialTheme.typography.labelMedium
//        )
//        Box(modifier = Modifier.height(6.dp))

        MyCheckbox("نمایش بازارها", onlyMarketsChecked.value) {
            onlyMarketsChecked.value = it
            setMarkets(allExchanges, checkedStates, it)

            if (allChecked.value)
                setAll(allExchanges, checkedStates, true)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetAppbar(hide: () -> Unit, title: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {

        IconButton(
            onClick = {
                hide()
            }
        ) {
            Icon(
                painterResource(id = R.drawable.ic_close),
                tint = Color.Black,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(18.dp),
                contentDescription = R.drawable.ic_close.toString()
            )
        }

        Text(
            title,
            modifier = Modifier.padding(end = 16.dp),
            fontSize = 16.sp
        )
    }
}


@Composable
fun ExchangesListWithSelectAll(
    modifier: Modifier,
    allExchanges: List<ExchangeInfo>,
    checkedStates: MutableMap<Exchanges, Boolean>,
    onSelectedExchangesChanged: (List<Exchanges>) -> Unit
) {

    LazyColumn(
        modifier = modifier
    ) {
        items(allExchanges) { info ->
            ExchangeRow(
                info = info,
                isChecked = checkedStates[info.exchange] ?: false,
                onCheckedChange = { isChecked ->
                    checkedStates[info.exchange] = isChecked
                    val selected: List<Exchanges> = allExchanges.filter { checkedStates[it.exchange] == true }.map { it.exchange }
                    onSelectedExchangesChanged(selected)
                }
            )
            Divider()
        }
    }
}

@Composable
fun ExchangeRow(
    info: ExchangeInfo,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = null, // Handled by row click
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary
            )
        )
        Box(
            modifier = Modifier.width(16.dp)
        )
        Image(
            painter = painterResource(id = info.exchange.logo),
            contentDescription = info.exchange.title,
            modifier = Modifier.size(26.dp)
        )
        Box(
            modifier = Modifier.width(16.dp)
        )
        Text(
            text = info.exchange.title,
        )
    }
}

@Preview
@Composable
fun AssetOptionsBottomSheetScreenPreview() {
    MyApplicationTheme {
//        AssetOptionsBottomSheetScreen(emptyList(), false, hide = {}, onSubmit = { a, b -> })
    }
}