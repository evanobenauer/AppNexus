package com.ejo.util.misc;

public class AnimationUtil {

    public static float getNextAnimationValue(boolean condition, float value, int min, int max, float speed) {
        if (condition) {
            if (value < max) value += speed;
        } else {
            if (value > min) value -= speed;
        }
        return Math.clamp(value,0,255);
    }

}
