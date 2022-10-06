package com.bloxbean.cardano.yaci.hello;

import com.bloxbean.cardano.yaci.core.common.Constants;
import com.bloxbean.cardano.yaci.core.helpers.BlockFetcher;
import com.bloxbean.cardano.yaci.core.helpers.TipFinder;
import com.bloxbean.cardano.yaci.core.protocol.chainsync.messages.Point;
import com.bloxbean.cardano.yaci.core.protocol.chainsync.messages.Tip;

/**
 * This example fetches blocks from point-1 to tip.
 *  1. Find the current tip using TipFinder
 *  2. Start BlockFetcher instance and receive blocks in a consumer function
 */
public class BlockFetchTest {

    public static void main(String[] args) {
        String cardanoNodeHost = Constants.MAINNET_IOHK_RELAY_ADDR;
        int cardanoNodePort = Constants.MAINNET_IOHK_RELAY_PORT;
        Point wellKnownPoint = Constants.WELL_KNOWN_MAINNET_POINT;
        long protocolMagic = Constants.MAINNET_PROTOCOL_MAGIC;

        TipFinder tipFinder = new TipFinder(cardanoNodeHost, cardanoNodePort, wellKnownPoint, protocolMagic);
        Tip tip = tipFinder.find().block();
        tipFinder.shutdown();

        System.out.println("Tip >> " + tip.getPoint());

        //Last byron block
        Point from = new Point(4492799, "f8084c61b6a238acec985b59310b6ecec49c0ab8352249afd7268da5cff2a457");
        Point to = tip.getPoint();

        BlockFetcher blockFetcher = new BlockFetcher(cardanoNodeHost, cardanoNodePort, protocolMagic);

        blockFetcher.start(block -> {
            System.out.println("Block >>> " + block.getHeader().getHeaderBody().getBlockNumber() + "  "
                    + block.getHeader().getHeaderBody().getSlot() + "  " + block.getEra()
                    + "   size: " + block.getHeader().getHeaderBody().getBlockBodySize());
        });

        blockFetcher.fetch(from, to);

        //blockFetcher.shutdown();
    }
}
