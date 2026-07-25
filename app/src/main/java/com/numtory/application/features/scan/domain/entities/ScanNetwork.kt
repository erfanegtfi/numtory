package com.numtory.application.features.scan.domain.entities


enum class ScanNetwork(
    val title: String,
    val symbol: String,
    val explorerTransaction: String,
    val explorerAddress: String,
) {
    bitcoin(
        title = "بیت‌کوین",
        symbol = "BTC",
        explorerTransaction = "https://www.blockchain.com/btc/tx/{txid}",
        explorerAddress = "https://www.blockchain.com/btc/address/{address}",
    ),
    ethereum(
        title = "اتریوم",
        symbol = "ETH",
        explorerTransaction = "https://etherscan.io/tx/{txid}",
        explorerAddress = "https://etherscan.io/address/{address}",
    ),
    tron(
        title = "ترون",
        symbol = "TRX",
        explorerTransaction = "https://tronscan.org/#/transaction/{txid}",
        explorerAddress = "https://tronscan.org/#/address/{address}",
    ),
    bsc(
        title = "بایننس اسمارت چین",
        symbol = "BSC",
        explorerTransaction = "https://bscscan.com/tx/{txid}",
        explorerAddress = "https://bscscan.com/address/{address}",
    ),
    polygon(
        title = "پالیگان",
        symbol = "MATIC",
        explorerTransaction = "https://polygonscan.com/tx/{txid}",
        explorerAddress = "https://polygonscan.com/address/{address}",
    ),
    ton(
        title = "تون",
        symbol = "TON",
        explorerTransaction = "https://tonviewer.com/transaction/{txid}/",
        explorerAddress = "https://tonviewer.com/{address}/",
    ),
    near(
        title = "نیر",
        symbol = "NEAR",
        explorerTransaction = "https://nearblocks.io/txns/{txid}",
        explorerAddress = "https://nearblocks.io/address/{address}",
    ),
    binance(
        title = "بایننس چین",
        symbol = "BNB",
        explorerTransaction = "https://explorer.binance.org/tx/{txid}",
        explorerAddress = "https://explorer.binance.org/address/{address}",
    ),
    ripple(
        title = "ریپل",
        symbol = "XRP",
        explorerTransaction = "https://xrpscan.com/tx/{txid}",
        explorerAddress = "https://xrpscan.com/account/{address}",
    ),
    cardano(
        title = "کاردانو",
        symbol = "ADA",
        explorerTransaction = "https://explorer.cardano.org/en/transaction?id={txid}",
        explorerAddress = "https://explorer.cardano.org/en/address.html?address={address}",
    ),
    solana(
        title = "سولانا",
        symbol = "SOL",
        explorerTransaction = "https://solscan.io/tx/{txid}",
        explorerAddress = "https://solscan.io/account/{address}",
    ),
    hedera(
        title = "هدرا",
        symbol = "HBAR",
        explorerTransaction = "https://hashscan.io/#/mainnet/transaction/{txid}",
        explorerAddress = "https://hashscan.io/#/mainnet/account/{address}?type=",
    );
}

/** Most chains hash transactions to 32 bytes, which is what an explorer shows as 64 hex chars. */
private val HEX_64 = Regex("^(0x)?[0-9a-fA-F]{64}$")

/**
 * Whether [query] reads as a transaction hash rather than an account address, so that a
 * single input box can serve both. The user types one thing; the shapes of the two are
 * different enough per network to tell apart without asking.
 */
fun ScanNetwork.isTransaction(query: String): Boolean {
    val value = query.trim()
    return when (this) {
        // 0.0.1234 is an account, 0.0.1234@1699999999.000000000 a transaction.
        ScanNetwork.hedera -> value.contains('@')
        // Base58 signatures run 87-88 characters; addresses stop at 44.
        ScanNetwork.solana -> value.length >= 80
        // Named accounts carry a dot (alice.near); hashes are 43-44 base58 characters.
        // Implicit accounts are 64 hex, so the hex rule below must not decide this one.
        ScanNetwork.near -> !value.contains('.') && value.length in 42..46
        else -> HEX_64.matches(value) || value.length >= 80
    }
}

/** The explorer page for [query] — its transaction page or its address page. */
fun ScanNetwork.explorerUrlFor(query: String): String {
    val value = query.trim()
    return if (isTransaction(value))
        explorerTransaction.replace("{txid}", value)
    else
        explorerAddress.replace("{address}", value)
}
