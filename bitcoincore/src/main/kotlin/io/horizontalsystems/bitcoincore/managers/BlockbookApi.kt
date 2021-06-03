package io.horizontalsystems.bitcoincore.managers

import io.horizontalsystems.bitcoincore.core.IInitialSyncApi
import java.util.logging.Logger

open class BlockbookApi(host: String, headers: List<Pair<String, String>> = listOf()) : IInitialSyncApi {
    private val apiManager = ApiManager(host, headers)
    private val logger = Logger.getLogger("BlockbookApi")

    override fun getTransactions(addresses: List<String>, xpub: String): List<TransactionItem> {
        logger.info("Request transactions for ${addresses.size} addresses: [${addresses.first()}, ...]")

        val transactions = mutableListOf<TransactionItem>()
        var page = 0
        var totalPages = 1
        while (page < totalPages) {
            page++
            val json = apiManager.doOkHttpGet(false, "api/v2/xpub/${xpub}?page=${page}&details=txs").asObject()
            totalPages = json["totalPages"].asInt()
            for (item in json["transactions"].asArray()) {
                val tx = item.asObject()

                val blockHash = tx["blockHash"] ?: continue
                val blockHeight = tx["blockHeight"] ?: continue

                val outputs = mutableListOf<TransactionOutputItem>()

                for (outputItem in tx["vout"].asArray()) {
                    val outputJson = outputItem.asObject()

                    val addrs = (outputJson["addresses"] ?: continue).asArray()
                    val address = addrs[0].asString()
                    if (!addresses.contains(address)) {
                        continue
                    }
                    val script = (outputJson["hex"] ?: continue).asString()

                    outputs.add(TransactionOutputItem(script, address))
                }

                if (outputs.isNotEmpty()) {
                    transactions.add(TransactionItem(blockHash.asString(), blockHeight.asInt(), outputs))
                }
            }
        }

        return transactions
    }
}