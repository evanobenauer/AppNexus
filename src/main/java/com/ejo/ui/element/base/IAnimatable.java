package com.ejo.ui.element.base;

public interface IAnimatable {

    void runAnimation(float speed); //Game plan for this is to eventually have it in a self-contained thread

    default void runAnimation() {
        runAnimation(getAnimationSpeed());
    }

    float getAnimationSpeed();
}
