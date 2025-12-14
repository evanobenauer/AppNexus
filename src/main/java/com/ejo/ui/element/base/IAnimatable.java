package com.ejo.ui.element.base;

public interface IAnimatable {

    void updateAnimation(float speed); //Game plan for this is to eventually have it in a self-contained thread

    default void updateAnimation() {
        updateAnimation(getAnimationSpeed());
    }

    float getAnimationSpeed();
}
