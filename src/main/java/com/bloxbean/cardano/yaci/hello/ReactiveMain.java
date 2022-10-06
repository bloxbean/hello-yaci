package com.bloxbean.cardano.yaci.hello;

import com.bloxbean.cardano.yaci.core.common.Constants;
import com.bloxbean.cardano.yaci.core.model.Block;
import com.bloxbean.cardano.yaci.core.protocol.handshake.util.N2NVersionTableConstant;
import com.bloxbean.cardano.yaci.core.reactive.BlockStreamer;
import reactor.core.publisher.Flux;

/**
 * Get incoming block data through reactive BlockStreamer api
 */
public class ReactiveMain {

    public void start() throws InterruptedException {
       BlockStreamer streamer = BlockStreamer.fromLatest(Constants.MAINNET_IOHK_RELAY_ADDR, Constants.MAINNET_IOHK_RELAY_PORT,
                Constants.WELL_KNOWN_MAINNET_POINT,
                N2NVersionTableConstant.v4AndAbove(Constants.MAINNET_PROTOCOL_MAGIC));

        /** Receive incoming blocks and print block size & no of txns **/
        Flux<Block> blockFlux = streamer.stream();
        blockFlux.subscribe(block -> {
            System.out.println("Block #   : " + block.getHeader().getHeaderBody().getBlockNumber());
            System.out.println("Block Size: " + block.getHeader().getHeaderBody().getBlockBodySize());
            System.out.println("# of Txns : " + block.getTransactionBodies().size());
            System.out.println("");
        });

        /** Alert when a transaction found at an address **/
        /**
        Flux<TransactionOutput> flux =  streamer.stream().map(Block::getTransactionBodies)
                .flatMap(transactionBodies ->
                        Flux.fromStream(transactionBodies.stream().map(transactionBody -> transactionBody.getOutputs())))
                .flatMap(transactionOutputs -> Flux.fromStream(transactionOutputs.stream()))
                .filter(transactionOutput -> transactionOutput.getAddress().equals("addr1w999n67e86jn6xal07pzxtrmqynspgx0fwmcmpua4wc6yzsxpljz3"));

         flux.subscribe(transactionOutput -> {
             System.out.println("Transaction found for  >> " + transactionOutput);
         }); **/

         while (true)
            Thread.sleep(50000);

//         streamer.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        new ReactiveMain().start();
    }
}
