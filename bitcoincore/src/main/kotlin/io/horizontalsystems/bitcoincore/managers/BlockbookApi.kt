package io.horizontalsystems.bitcoincore.managers

import io.horizontalsystems.bitcoincore.core.IInitialSyncApi
import java.util.logging.Logger

class BlockbookApi(host: String, headers: List<Pair<String, String>> = listOf()) : IInitialSyncApi {
    private val apiManager = ApiManager(host, headers)
    private val logger = Logger.getLogger("BlockbookApi")

    override fun getTransactions(addresses: List<String>, xpub: String): List<TransactionItem> {
        logger.info("Request transactions for ${addresses.size} addresses: [${addresses.first()}, ...]")

        val transactions = mutableListOf<TransactionItem>()
        var page = 0
        var totalPages = 1
        while (page < totalPages) {
            page++
            val json = apiManager.doOkHttpGet(false, "/api/v2/xpub/${xpub}?page=${page}&details=txs&tokens=derived").asObject()
            totalPages = json["totalPages"].asInt()
            val tokens = json["tokens"].asArray()
        }

        return transactions
    }
}