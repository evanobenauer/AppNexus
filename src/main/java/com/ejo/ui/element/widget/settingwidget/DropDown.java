package com.ejo.ui.element.widget.settingwidget;

import com.ejo.ui.Scene;
import com.ejo.ui.element.Text;
import com.ejo.ui.element.polygon.ConvexPolygon;
import com.ejo.ui.element.polygon.Rectangle;
import com.ejo.ui.element.polygon.RoundedRectangle;
import com.ejo.ui.element.widget.Widget;
import com.ejo.ui.handler.MouseHoveredHandler;
import com.ejo.ui.render.GLUtil;
import com.ejo.util.math.Angle;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.AnimationUtil;
import com.ejo.util.misc.ColorUtil;
import com.ejo.util.setting.Container;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DropDown<T> extends SettingWidget<T>{

    private Color color;

    private final ArrayList<T> items;

    //Selection Variables
    private final MouseHoveredHandler selectionMouseHoveredHandler;
    private final ArrayList<SelectionBoxWidget> selectionBoxes;

    //Open Variables
    private boolean open;
    private float openPercent;


    @SafeVarargs
    public DropDown(Scene scene, Vector pos, Vector size, Color color, String title, String description, Container<T> container, T... items) {
        super(scene, pos, size, container, () -> {}, title, description);
        this.color = color;

        this.items = new ArrayList<>(Arrays.asList(items));

        //Add selection boxes to list
        this.selectionMouseHoveredHandler = new MouseHoveredHandler();
        this.selectionBoxes = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            int finalI = i;
            selectionBoxes.add(new SelectionBoxWidget(scene, Vector.NULL(), new RoundedRectangle(scene, Vector.NULL(), getSize().getSubtracted(3, 3), new Color(0,0,0,0)), () -> getContainer().set(this.items.get(finalI))));
        }

        //Open variables
        this.open = false;
        this.openPercent = 0;
    }

    @SafeVarargs
    public DropDown(Scene scene, Vector pos, Vector size, Color color, Container<T> container, T... items) {
        this(scene, pos, size, color, "","",container, items);
    }

    @Override
    protected void drawWidget(Vector mousePos) {
        int border = getHeadSize().getYi() / 5;

        //Draw Drop-Down
        RoundedRectangle dropDown = new RoundedRectangle(getScene(),getPos(),getSize(),new Color(25,25,25,150));
        dropDown.setCornerRadius(5);
        dropDown.draw();

        //Draw Drop-Down Outline
        int offset = 1;
        RoundedRectangle dropDownOutline = new RoundedRectangle(getScene(),getPos().getAdded(offset,offset),getSize().getSubtracted(new Vector(offset,offset).getMultiplied(2)),color,5,true,2);
        dropDownOutline.setCornerRadius(5);
        dropDownOutline.draw();

        //Draw Items
        int itemY = getHeadSize().getYi();
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            int textSize = getHeadSize().getYi() - border;
            Vector pos = getPos().getAdded(2,itemY * openPercent);

            //Draw Text
            Text text = new Text(getScene(), pos.getAdded(border + 2, getHeadSize().getY() / 2 - textSize / 2), item.toString(), new Font("Arial", Font.PLAIN, textSize), Color.WHITE, Text.Type.STATIC);
            text.draw();

            //Draw Highlight
            updateDrawSelectionBox(i,pos,mousePos);

            //Increment next Y position
            itemY += getHeadSize().getYi();
        }

        //Update Local MouseHoveredHandler
        if (this.baseShape.getMouseHoveredCalculation(mousePos))
            selectionMouseHoveredHandler.queueHoverable(this.baseShape);
        selectionMouseHoveredHandler.cycleQueuedElements();

        //Draw Header
        ((RoundedRectangle)this.baseShape).setCornerRadius(5);
        this.baseShape.setColor(ColorUtil.getWithAlpha(SETTING_WIDGET_BACKGROUND_COLOR,255));
        this.baseShape.draw();

        //Draw Header Arrow
        float arrowSize = getHeadSize().getYf() - border * 2f;
        Vector arrowPos = getPos().getAdded(getSize().getX() - arrowSize - border,border);
        Vector centerArrowPos = arrowPos.getAdded(arrowSize / 2,arrowSize / 2);
        Angle rot = new Angle(Math.TAU/4 * openPercent);
        GLUtil.rotateAboutPoint(centerArrowPos,rot,() -> drawRArrow(arrowPos.getSubtracted(centerArrowPos),arrowSize,color));

        //Draw Selected As Title
        String title = getTitle() + (!getTitle().isEmpty() ? ": " : "") + getContainer().get();
        SettingWidget.drawWidgetTitle(getScene(),getPos(),getHeadSize(),title,border,false,Color.WHITE);
    }

    @Override
    public void updateAnimation(float speed) {
        super.updateAnimation(speed);
        this.openPercent = AnimationUtil.getNextAnimationValue(open,openPercent,0,1,.1f);
    }

    @Override
    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
        if (action == GLFW.GLFW_RELEASE && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (isHeaderHovered(mousePos)) {
                open = !open;
                getAction().run();
            }
        }
        for (SelectionBoxWidget widget : selectionBoxes)
            widget.onMouseClick(button, action, mods, mousePos);
    }

    @Override
    public void onKeyPress(int key, int scancode, int action, int mods) {
        //NA
    }

    @Override
    public void onMouseScroll(double scroll, Vector mousePos) {
        //NA
    }

    // ===============================

    // MOUSE HOVERED METHODS

    // ===============================

    @Override
    public boolean getMouseHoveredCalculation(Vector mousePos) {
        return Rectangle.isInRectangleBoundingBox(getPos(),getSize(),mousePos);
    }

    private boolean isHeaderHovered(Vector mousePos) {
        return Rectangle.isInRectangleBoundingBox(getPos(),getHeadSize(),mousePos);
    }


    // ===============================

    // UPDATE/DRAW METHODS

    // ===============================

    protected void updateDrawSelectionBox(int i, Vector pos, Vector mousePos) {
        SelectionBoxWidget selectionBox = selectionBoxes.get(i);

        //Update Selection Box
        selectionBox.updateMouseHovered(selectionMouseHoveredHandler,mousePos);
        selectionBox.updateAnimation(10);

        //Draw Selection Box
        selectionBox.setPos(pos.getAdded(1,1));
        selectionBox.getRect().setSize(getHeadSize().getSubtracted(4,4));
        selectionBox.draw();
    }

    //TODO: Rotate this up/down for the dropdown
    private void drawRArrow(Vector pos, float size, Color color) {
        float depth = size / 2;
        ConvexPolygon top = new ConvexPolygon(getScene(),pos,color,
                new Vector(0,0),
                new Vector(size - depth,0),
                new Vector(size,size / 2),
                new Vector(depth,size / 2));
        ConvexPolygon bottom = new ConvexPolygon(getScene(),pos,color,
                new Vector(depth,size / 2),
                new Vector(size,size / 2),
                new Vector(size - depth,size),
                new Vector(0,size));
        top.draw();
        bottom.draw();
    }

    // ===============================

    // GETTERS/SETTERS

    // ===============================

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    public boolean isOpen() {
        return open;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public Vector getSize() {
        return new Vector(getHeadSize().getX(),getHeadSize().getY() + getHeadSize().getY() * items.size() * openPercent);
    }

    public Vector getHeadSize() {
        return ((Rectangle) baseShape).getSize();
    }




    private class SelectionBoxWidget extends Widget {

        public SelectionBoxWidget(Scene scene, Vector pos, ConvexPolygon baseShape, Runnable action) {
            super(scene, pos, baseShape, action);
        }

        @Override
        protected void drawWidget(Vector mousePos) {
        }

        @Override
        public void onKeyPress(int key, int scancode, int action, int mods) {
        }

        @Override
        public void onMouseClick(int button, int action, int mods, Vector mousePos) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS)
                if (isMouseHovered()) {
                    DropDown.this.getAction().run();
                    getAction().run();
                    this.hoverHighlightFade = 175f;
                }
        }

        @Override
        public void onMouseScroll(double scroll, Vector mousePos) {
        }

        @Override
        public void updateAnimation(float speed) {
            if (this.hoverHighlightFade > 21) hoverHighlightFade -= speed;
            this.hoverHighlightFade = AnimationUtil.getNextAnimationValue(isMouseHovered(), hoverHighlightFade, 0, 20, speed);
        }

        public RoundedRectangle getRect() {
            return (RoundedRectangle) this.baseShape;
        }
    }
}
