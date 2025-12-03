package com.ejo.util.action;

/**
 * DoOnce is a class that will run an action a single time until reset
 */
public class DoOnce {

    private boolean shouldRun;

    public DoOnce(boolean runImmediately) {
        this.shouldRun = runImmediately;
    }

    public DoOnce() {
        this(true);
    }

    public void run(Runnable action) {
        if (shouldRun) {
            action.run();
            this.shouldRun = false;
        }
    }

    public void reset() {
        shouldRun = true;
    }

}