package com.ejo.util.time;

import com.ejo.util.math.MathUtil;
import java.util.LinkedList;

public class TickRateLogger {

    private final StopWatch watch;

    private int ticks;
    private float tickRate;

    private final float frequencyS;

    private final LinkedList<Float> avgList;
    private int avgLength;

    public TickRateLogger(float frequencyS, int avgLength) {
        this.watch = new StopWatch();
        this.ticks = 0;
        this.tickRate = 0;
        this.frequencyS = frequencyS;
        this.avgLength = avgLength;
        this.avgList = new LinkedList<>();
    }

    public TickRateLogger(float frequencyS) {
        this(frequencyS,1);
    }

    public TickRateLogger() {
        this(1);
    }

    public void tick() {
        ticks++;
    }

    public void updateTickRate() {
        watch.start();
        if (watch.hasTimePassedS(frequencyS)) {
            tickRate = ticks * (1 / frequencyS);
            ticks = 0;

            avgList.addLast(tickRate);
            if (avgList.size() > avgLength) avgList.poll();

            watch.restart();
        }

    }

    public void setAvgLength(int avgLength) {
        this.avgLength = avgLength;
    }

    public float getAverageTickRate() {
        return (float)MathUtil.calculateAverage(avgList.toArray(new Float[0]));
    }

    public float getTickRate() {
        return tickRate;
    }

}
