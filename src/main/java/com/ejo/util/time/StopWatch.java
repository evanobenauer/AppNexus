package com.ejo.util.time;

import com.ejo.util.math.MathUtil;

/**
 * The StopWatch class is a very simple stop watch. It is able to start, stop, restart, and get the time at any point in its process.
 * This is very useful for simply timing when you want things to occur, especially in loops
 */
public class StopWatch {

    private boolean running;
    private long startSystemTime;
    private long stopTime;

    public StopWatch() {
        this.running = false;
        this.startSystemTime = -1;
        this.stopTime = -1;
    }

    public void start() {
        if (!isRunning()) restart();
    }

    public void stop() {
        this.running = false;
        this.stopTime = System.currentTimeMillis() - this.startSystemTime;
        this.startSystemTime = -1;
    }

    public void restart() {
        this.startSystemTime = System.currentTimeMillis();
        this.stopTime = -1;
        this.running = true;
    }


    public void run(int timeMillis, Runnable action) {
        start();
        if (hasTimePassedMS(timeMillis)) {
            action.run();
            restart();
        }
    }


    public boolean isRunning() {
        return running;
    }

    public boolean hasTimePassedMS(double time) {
        return isRunning() && time < getCurrentTimeMS();
    }

    public boolean hasTimePassedS(double time) {
        return isRunning() && time * 1000 < getCurrentTimeMS();
    }


    public double getCurrentTimeMS() {
        return System.currentTimeMillis() - this.startSystemTime;
    }

    public double getCurrentTimeS() {
        return MathUtil.roundDouble(((float) getCurrentTimeMS()) / 1000,2);
    }

    public double getStopTimeMS() {
        return stopTime;
    }

    public double getStopTimeS() {
        return MathUtil.roundDouble(((float) getCurrentTimeMS()) / 1000,2);
    }

}
