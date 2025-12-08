package com.ejo.ui.element.base;

import com.ejo.util.math.Vector;

public interface IInteractable {

    void onKeyPress(int key, int scancode, int action, int mods);

    void onMouseClick(int button, int action, int mods, Vector mousePos);

    void onMouseScroll(int scroll, Vector mousePos);

}
