package com.ejo.ui.scene.manager.scenemanager;

import com.ejo.ui.element.elements.shape.RoundedRectangle;
import com.ejo.ui.scene.Scene;
import com.ejo.ui.scene.manager.FontManager;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.ColorUtil;

import java.awt.*;
import java.util.ArrayList;

//Think about moving all notifications to the bottom right of the screen, have them stack up from the bottom
public class NotificationManager extends SceneManager {

    private final ArrayList<Notification> notifications;
    private final ArrayList<Notification> queuedNotificationsRemoval;

    private int maximumNotifications;

    private FontManager fontManager;

    public NotificationManager(Scene scene, int notificationSize, int maximumNotifications) {
        super(scene);
        this.maximumNotifications = maximumNotifications;

        this.notifications = new ArrayList<>();
        this.queuedNotificationsRemoval = new ArrayList<>();

        this.fontManager = new FontManager("Arial", Font.PLAIN,notificationSize);
    }

    // =================================================

    // USABLE METHODS

    // =================================================

    public void draw() {
        cycleQueuedItems();

        int y = 2;
        int border = fontManager.getFont().getSize() / 5;
        for (Notification notification : notifications) {

            double width = fontManager.getWidth(scene,notification.text);
            Vector pos = new Vector(scene.getWindow().getSize().getX() / 2 - width / 2,y);

            new RoundedRectangle(scene, pos.getSubtracted(border,border), new Vector(width + border * 2, fontManager.getFont().getSize() + border * 2), new Color(0, 0, 0,(int)Math.clamp(notification.fade,0,175))).draw();
            new RoundedRectangle(scene, pos.getSubtracted(border,border), new Vector(width + border * 2, fontManager.getFont().getSize() + border * 2), new Color(150, 150, 150, (int)Math.clamp(notification.fade,0,175)),30,true,2).draw();
            fontManager.drawStaticString(scene, notification.text, pos, notification.getFadeColor());

            y += fontManager.getFont().getSize() + border * 2;

            notification.updateFadeOut();
            queueNotificationRemoval(notification);
        }
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
        this.fontManager = new FontManager(font);
    }

    public int getMaximumNotifications() {
        return maximumNotifications;
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

        void updateFadeOut() {
            this.fade = Math.clamp(fade - fadeSpeed, 0, 255);
        }

        Color getFadeColor() {
            return ColorUtil.getWithAlpha(color,(int) fade);
        }
    }
}
