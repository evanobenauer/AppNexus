package com.ejo.ui.manager;

import com.ejo.ui.element.shape.RoundedRectangle;
import com.ejo.ui.Scene;
import com.ejo.ui.render.FontRenderer;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.ColorUtil;

import java.awt.*;
import java.util.ArrayList;

//Think about moving all notifications to the bottom right of the screen, have them stack up from the bottom
public class NotificationManager extends SceneManager {

    private final ArrayList<Notification> notifications;
    private final ArrayList<Notification> queuedNotificationsRemoval;

    private int maximumNotifications;

    private FontRenderer fontRenderer;

    public NotificationManager(Scene scene, int notificationSize, int maximumNotifications) {
        super(scene);
        this.maximumNotifications = maximumNotifications;

        this.notifications = new ArrayList<>();
        this.queuedNotificationsRemoval = new ArrayList<>();

        this.fontRenderer = new FontRenderer("Arial", Font.PLAIN,notificationSize);
    }

    // =================================================

    // USABLE METHODS

    // =================================================

    @Override
    public void draw(Vector mousePos) {
        cycleQueuedItems();

        int y = 2;
        int border = fontRenderer.getFont().getSize() / 5;
        for (Notification notification : notifications) {
            notification.draw(scene,fontRenderer,border,y);
            y += fontRenderer.getFont().getSize() + border * 2;

            queueNotificationRemoval(notification);
        }
    }

    @Override
    public void updateAnimation(float speed) {
        for (Notification notification : notifications)
            notification.updateAnimation();
    }

    public void sendNotification(String text, Color color, float fadeSpeed) {
        notifications.add(new Notification(text, color, fadeSpeed));
    }

    public void sendNotification(String text, Color color) {
        sendNotification(text,color,1);
    }

    // =================================================

    // INTERNAL METHODS

    // =================================================

    private void queueNotificationRemoval(Notification notification) {
        if (notification.fade <= 0) queuedNotificationsRemoval.add(notification);
    }

    private void cycleQueuedItems() {
        int removals = Math.clamp(notifications.size() - maximumNotifications,0, notifications.size());
        for (int i = 0; i < removals; i++) queuedNotificationsRemoval.add(notifications.get(i));
        notifications.removeAll(queuedNotificationsRemoval);
    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void setMaximumNotifications(int maximumNotifications) {
        this.maximumNotifications = maximumNotifications;
    }

    public void setFont(Font font) {
        this.fontRenderer = new FontRenderer(font);
    }

    private static class Notification {
        final String text;
        final Color color;
        final float fadeSpeed;

        float fade;

        Notification(String text, Color color, float fadeSpeed) {
            this.text = text;
            this.color = color;
            this.fadeSpeed = fadeSpeed;

            this.fade = 255f;
        }

        void draw(Scene scene, FontRenderer fontRenderer, float border, int y) {
            double width = fontRenderer.getWidth(text);
            Vector pos = new Vector(scene.getWindow().getSize().getX() / 2 - width / 2,y);

            new RoundedRectangle(scene, pos.getSubtracted(border,border), new Vector(width + border * 2, fontRenderer.getFont().getSize() + border * 2), new Color(0, 0, 0,(int)Math.clamp(fade,0,175))).draw();
            new RoundedRectangle(scene, pos.getSubtracted(border,border), new Vector(width + border * 2, fontRenderer.getFont().getSize() + border * 2), new Color(150, 150, 150, (int)Math.clamp(fade,0,175)),30,true,2).draw();
            fontRenderer.drawStaticString(text, pos, getFadeColor());
        }

        void updateAnimation() {
            this.fade = Math.clamp(fade - fadeSpeed, 0, 255);
        }

        Color getFadeColor() {
            return ColorUtil.getWithAlpha(color,(int) fade);
        }
    }
}
