package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo
import com.numtory.application.features.gold.domain.entities.GoldMarketPrice
import com.numtory.application.features.gold.domain.enums.GoldExchanges
import com.numtory.application.ui.theme.GOLD
import com.numtory.application.ui.theme.SILVER
import kotlinx.coroutines.flow.Flow

/** The symbols each exchange spells its own way. */
private const val DARIC_GOLD = "GOLD18TMN"
private const val DARIC_SILVER = "SILVERTMN"
private const val ECOGOLD_GOLD = "GOLD18-IRT"
private const val ECOGOLD_SILVER = "SILVER999-IRT"

/**
 * Knows which exchanges quote which metal, and which of them are worth asking right now.
 */
class GetGoldPriceFlowsUseCase constructor(
    private val getDigikalaPriceUseCase: GetDigikalaPriceUseCase,
    private val getGoldikaPriceUseCase: GetGoldikaPriceUseCase,
    private val getTlynPriceUseCase: GetTlynPriceUseCase,
    private val getHamrahGoldPriceUseCase: GetHamrahGoldPriceUseCase,
    private val getMelliGoldPriceUseCase: GetMelliGoldPriceUseCase,
    private val getTalaseaPriceUseCase: GetTalaseaPriceUseCase,
    private val getWallGoldPriceUseCase: GetWallGoldPriceUseCase,
    private val getMilliPriceUseCase: GetMilliPriceUseCase,
    private val getTechnoGoldPriceUseCase: GetTechnoGoldPriceUseCase,
    private val getDaricPriceUseCase: GetDaricPriceUseCase,
    private val getEcoGoldPriceUseCase: GetEcoGoldPriceUseCase,
    private val getZarminexPriceUseCase: GetZarminexPriceUseCase,
    private val getNoghreseaPriceUseCase: GetNoghreseaPriceUseCase,
    private val getGeramiPriceUseCase: GetGeramiPriceUseCase,
    private val getZarafzaPriceUseCase: GetZarafzaPriceUseCase,
) {

    private class PriceSource(
        val exchange: GoldExchanges,
        val open: () -> Flow<ApiCallResult<GoldMarketPrice>>,
    )


    fun action(
        symbol: String,
        exchangesInfo: List<GoldExchangeInfo>?,
    ): List<Flow<ApiCallResult<GoldMarketPrice>>> =
        sourcesFor(symbol)
            .filter { source ->
                exchangesInfo.isNullOrEmpty() ||
                        exchangesInfo.firstOrNull { it.exchange == source.exchange }?.active == true
            }
            .map { it.open() }

    private fun sourcesFor(symbol: String): List<PriceSource> = when (symbol) {
        GOLD -> listOf(
            PriceSource(GoldExchanges.digikala) { getDigikalaPriceUseCase.action() },
            PriceSource(GoldExchanges.goldika) { getGoldikaPriceUseCase.action() },
            PriceSource(GoldExchanges.taline) { getTlynPriceUseCase.action() },
            PriceSource(GoldExchanges.hamrahgold) { getHamrahGoldPriceUseCase.action() },
            PriceSource(GoldExchanges.melligold) { getMelliGoldPriceUseCase.action() },
            PriceSource(GoldExchanges.talasea) { getTalaseaPriceUseCase.action() },
            PriceSource(GoldExchanges.wallgold) { getWallGoldPriceUseCase.action() },
            PriceSource(GoldExchanges.milli) { getMilliPriceUseCase.action() },
            PriceSource(GoldExchanges.technoGold) { getTechnoGoldPriceUseCase.action() },
            PriceSource(GoldExchanges.zarminex) { getZarminexPriceUseCase.action() },
            PriceSource(GoldExchanges.daric) { getDaricPriceUseCase.action(DARIC_GOLD) },
            PriceSource(GoldExchanges.ecogold) { getEcoGoldPriceUseCase.action(ECOGOLD_GOLD) },
            PriceSource(GoldExchanges.gerami) { getGeramiPriceUseCase.action(GOLD) },
            PriceSource(GoldExchanges.zarafza) { getZarafzaPriceUseCase.action() },
        )

        SILVER -> listOf(
            PriceSource(GoldExchanges.daric) { getDaricPriceUseCase.action(DARIC_SILVER) },
            PriceSource(GoldExchanges.ecogold) { getEcoGoldPriceUseCase.action(ECOGOLD_SILVER) },
            PriceSource(GoldExchanges.noghresea) { getNoghreseaPriceUseCase.action() },
            PriceSource(GoldExchanges.gerami) { getGeramiPriceUseCase.action(SILVER) },
        )

        else -> emptyList()
    }
}
