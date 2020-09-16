package io.horizontalsystems.bitcoincore.managers

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.horizontalsystems.bitcoincore.core.Wallet
import io.horizontalsystems.bitcoincore.extensions.hexToByteArray
import io.horizontalsystems.bitcoincore.network.Network
import io.horizontalsystems.hdwalletkit.HDWallet
import io.horizontalsystems.hdwalletkit.Mnemonic
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner


@RunWith(PowerMockRunner::class)
class PublicKeyManagerTest {
    private lateinit var instance: PublicKeyManager

    @Before
    fun setUp() {
        val seed = "851a481d59b3d86f007b6c722641cb0990e22a2fb7eeba6787a2c4de8c7dce589ff952739e1af6801463eac1400868cf97bf2c8389c84b1ec8775f9db941d520".hexToByteArray()
        val hdwallet = HDWallet(seed, 1)
        val network = mock<Network>()
        whenever(network.bip32HeaderPub).thenReturn(0x043587CF)
        val wallet = Wallet(hdwallet)
        instance = PublicKeyManager(mock(), wallet, RestoreKeyConverterChain(), network)
    }

    @Test
    fun testXpubIsCorrect() {
        val xpub = instance.extendedPublicKey(0)
        Assert.assertEquals("tpubDDiJkLNuryvrUPXn6uSkfQeaWveMKmTPWGfNBx1pax95XagZ1jtNKXAiey9h5hi3ZLLjqaBzAKZk6qKiF1zk7VVeQog9AGkjPRCDUtWRMDm", xpub)
    }
}