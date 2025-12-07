package com.ejo.util.misc;

//TODO: Implement this more
public class ThreadUtil {

    public static void sleepThread(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
