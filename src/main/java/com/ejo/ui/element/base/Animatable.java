package com.ejo.ui.element.base;

public interface Animatable {

    void updateAnimation(float speed);

    default float getAnimationSpeed() {
        return 1f;
    }
}
