package io.horizontalsystems.bitcoincore.core

import io.horizontalsystems.bitcoincore.crypto.Base58
import io.horizontalsystems.bitcoincore.extensions.hexToByteArray
import io.horizontalsystems.bitcoincore.models.PublicKey
import io.horizontalsystems.bitcoincore.utils.Utils
import io.horizontalsystems.hdwalletkit.HDKey
import io.horizontalsystems.hdwalletkit.HDWallet
import java.lang.Exception
import java.nio.ByteBuffer
import java.util.*

class Wallet(private val hdWallet: HDWallet) {

    val gapLimit = hdWallet.gapLimit

    fun publicKey(account: Int, index: Int, external: Boolean): PublicKey {
        val hdPubKey = hdWallet.hdPublicKey(account, index, external)
        return PublicKey(account, index, external, hdPubKey.publicKey, hdPubKey.publicKeyHash)
    }

    fun publicKeys(account: Int, indices: IntRange, external: Boolean): List<PublicKey> {
        val hdPublicKeys = hdWallet.hdPublicKeys(account, indices, external)

        if (hdPublicKeys.size != indices.count()) {
            throw HDWalletError.PublicKeysDerivationFailed()
        }

        return indices.mapIndexed { position, index ->
            val hdPublicKey = hdPublicKeys[position]
            PublicKey(account, index, external, hdPublicKey.publicKey, hdPublicKey.publicKeyHash)
        }
    }

    fun rootPrivateKey(account: Int): HDKey {
        return hdWallet.rootPrivateKey(account)
    }

    fun extendedPublicKey(account: Int): ByteArray {
        return hdWallet.extendedPublicKey(account)
    }

    open class HDWalletError : Exception() {
        class PublicKeysDerivationFailed : HDWalletError()
    }

}
