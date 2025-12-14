package com.ejo.util.misc;

public class AnimationUtil {

    public static float getNextAnimationValue(boolean condition, float fade, int min, int max, float speed) {
        if (condition) {
            if (fade < max) fade += speed;
        } else {
            if (fade > min) fade -= speed;
        }
        return Math.clamp(fade,0,255);
    }

}
