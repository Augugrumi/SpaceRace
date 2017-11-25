package com.augugrumi.spacerace.listener;

/**
 * @author Marco Zanella
 * @version 0.01
 *          date 25/11/17
 */

public interface EndMatchReceiver {
    String END_MATCH = "end-" + EndMatchReceiver.class.toString();
    void endMatch();
}
