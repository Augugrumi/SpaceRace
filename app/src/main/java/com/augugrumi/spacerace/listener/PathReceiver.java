package com.augugrumi.spacerace.listener;

/**
 * @author Marco Zanella
 * @version 0.01
 *          date 21/11/17
 */

public interface PathReceiver {
    String ACK_PATH = "ack_path_received-" + PathReceiver.class.toString();

    void receivePath(String s);
    void receiveAck();
}
