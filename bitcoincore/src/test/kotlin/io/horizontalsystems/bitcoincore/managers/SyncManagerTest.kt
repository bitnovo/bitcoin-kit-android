package io.horizontalsystems.bitcoincore.managers

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.horizontalsystems.bitcoincore.network.peer.PeerGroup
import org.mockito.Mockito.reset
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SyncManagerTest : Spek({

    val peerGroup = mock<PeerGroup>()
    val initialSyncer = mock<InitialSyncer>()

    val syncManager by memoized {
        SyncManager(peerGroup, initialSyncer)
    }

    afterEachTest {
        reset(peerGroup, initialSyncer)
    }

    describe("#start") {
        it("starts :initialSyncer") {
            syncManager.start()

            verify(initialSyncer).sync()
        }
    }

    describe("#stop") {
        it("stops :peerGroup") {
            syncManager.stop()

            verify(peerGroup).close()
        }

        it("stops :initialSyncer") {
            syncManager.stop()

            verify(peerGroup).close()
        }
    }

    describe("#onSyncingFinished") {
        it("starts peer group") {
            syncManager.onSyncingFinished()

            verify(peerGroup).start()
        }
    }

})
