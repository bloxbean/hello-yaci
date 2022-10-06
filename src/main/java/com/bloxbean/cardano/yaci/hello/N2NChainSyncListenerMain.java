package com.bloxbean.cardano.yaci.hello;

import com.bloxbean.cardano.yaci.core.common.Constants;
import com.bloxbean.cardano.yaci.core.helpers.N2NChainSyncFetcher;
import com.bloxbean.cardano.yaci.core.model.Block;
import com.bloxbean.cardano.yaci.core.model.BlockHeader;
import com.bloxbean.cardano.yaci.core.protocol.blockfetch.BlockfetchAgentListener;
import com.bloxbean.cardano.yaci.core.protocol.chainsync.messages.Point;
import com.bloxbean.cardano.yaci.core.protocol.chainsync.messages.Tip;
import com.bloxbean.cardano.yaci.core.protocol.chainsync.n2n.ChainSyncAgentListener;

/**
 * Start N2NChainSync protocol and receive incoming blocks in a listener
 */
public class N2NChainSyncListenerMain {

    public void start() throws InterruptedException {
        N2NChainSyncFetcher chainSyncFetcher = new N2NChainSyncFetcher(Constants.MAINNET_IOHK_RELAY_ADDR, Constants.MAINNET_IOHK_RELAY_PORT,
                Constants.WELL_KNOWN_MAINNET_POINT, Constants.MAINNET_PROTOCOL_MAGIC);

        //Attach BlockfetchAgent Listener
        chainSyncFetcher.addBlockFetchListener(new BlockfetchAgentListener() {
            @Override
            public void blockFound(Block block) {
                System.out.println("Received Block >> " + block.getHeader().getHeaderBody().getBlockNumber());
                System.out.println("Total # of Txns >> " + block.getTransactionBodies().size());
            }

            @Override
            public void noBlockFound() {
                System.out.println("No block found");
            }
        });

        //Attach ChainSyncAgent listener to listen rollback and rollforward event
        chainSyncFetcher.addChainSyncListener(new ChainSyncAgentListener() {
            @Override
            public void rollforward(Tip tip, BlockHeader blockHeader) {
                System.out.println("RollForward >> " + tip);
            }

            @Override
            public void rollbackward(Tip tip, Point toPoint) {
                System.out.println("Rollbakcward >> " + toPoint);
            }
        });

        //Start chain sync
        chainSyncFetcher.start();

        //Don't exit the main thread
        while (true)
            Thread.sleep(10000);

        //chainSyncFetcher.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        new N2NChainSyncListenerMain().start();
    }
}
