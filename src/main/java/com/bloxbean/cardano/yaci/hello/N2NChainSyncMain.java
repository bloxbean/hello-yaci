package com.bloxbean.cardano.yaci.hello;

import com.bloxbean.cardano.yaci.core.common.Constants;
import com.bloxbean.cardano.yaci.core.helpers.N2NChainSyncFetcher;

/**
 * Start N2NChainSync protocol and recieve incoming blocks in a consumer function
 */
public class N2NChainSyncMain {

    public void start() throws InterruptedException {
        N2NChainSyncFetcher chainSyncFetcher = new N2NChainSyncFetcher("192.168.0.228", 6000,
                Constants.WELL_KNOWN_MAINNET_POINT, Constants.MAINNET_PROTOCOL_MAGIC);

        chainSyncFetcher.start(block -> {
            System.out.println("Received Block >> " + block.getHeader().getHeaderBody().getBlockNumber());
            System.out.println("Total # of Txns >> " + block.getTransactionBodies().size());
        });

        while (true)
            Thread.sleep(10000);

    }

    public static void main(String[] args) throws InterruptedException {
        new N2NChainSyncMain().start();
    }
}
