package com.ejo.util.time;

public class TickRateLogger {

    private final StopWatch watch;

    private int ticks;
    private float tickRate;

    private final float frequencyS;

    public TickRateLogger(float frequencyS) {
        this.watch = new StopWatch();
        this.ticks = 0;
        this.tickRate = 0;
        this.frequencyS = frequencyS;
    }

    public TickRateLogger() {
        this(1);
    }

    public void tick() {
        ticks++;
    }

    //Needs to be placed in a maintenance loop in order to see the frequency for a window
    public void updateTickRate() {
        watch.start();
        if (watch.hasTimePassedS(frequencyS)) { //TPS-FPS Updater
            tickRate = ticks * (1 / frequencyS);
            ticks = 0;
            watch.restart();
        }
    }

    public float getTickRate() {
        return tickRate;
    }

}
