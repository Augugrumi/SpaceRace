package com.augugrumi.spacerace.listener;

/**
 * @author Marco Zanella
 * @version 0.01
 *          date 25/11/17
 */

public interface EndMatchReceiver {
    String END_MATCH = "end-" + EndMatchReceiver.class.toString();
    String ACK_END_MATCH = "ack_end_match-" + EndMatchReceiver.class.toString();

    void receiveEndMatch(String message);
    void receiveAckEndMatch(String message);
}
