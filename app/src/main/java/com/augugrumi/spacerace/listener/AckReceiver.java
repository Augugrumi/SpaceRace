package com.augugrumi.spacerace.listener;

import com.augugrumi.spacerace.MapActivity;

/**
 * @author Marco Zanella
 * @version 0.01
 *          date 24/11/17
 */

public interface AckReceiver {
    String ACK = "ack-" + AckReceiver.class.toString();
    void receiveAck();
}
