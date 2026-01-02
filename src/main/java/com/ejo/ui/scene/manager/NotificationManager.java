package com.ejo.ui.scene.manager;

import com.ejo.ui.element.elements.shape.RoundedRectangle;
import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.ColorUtil;

import java.awt.*;
import java.util.ArrayList;

//Think about moving all notifications to the bottom right of the screen, have them stack up from the bottom
public class NotificationManager {

    private final ArrayList<Notification> notifications;
    private final ArrayList<Notification> queuedNotificationsRemoval;

    private final Scene scene;

    private FontManager notificationFontManager;

    private int maximumNotifications;

    public NotificationManager(Scene scene, int notificationSize, int maximumNotifications) {
        this.scene = scene;
        this.maximumNotifications = maximumNotifications;

        this.notifications = new ArrayList<>();
        this.queuedNotificationsRemoval = new ArrayList<>();

        this.notificationFontManager = new FontManager("Arial", Font.PLAIN,notificationSize);
    }

    public void drawNotifications() {
        cycleQueuedItems();

        int y = 2;
        int border = notificationFontManager.getFont().getSize() / 5;
        for (Notification notification : notifications) {

            double width = notificationFontManager.getWidth(scene,notification.text);
            Vector pos = new Vector(scene.getWindow().getSize().getX() / 2 - width / 2,y);

            new RoundedRectangle(scene, pos.getSubtracted(border,border), new Vector(width + border * 2,notificationFontManager.getFont().getSize() + border * 2), new Color(0, 0, 0,(int)Math.clamp(notification.fade,0,175))).draw();
            new RoundedRectangle(scene, pos.getSubtracted(border,border), new Vector(width + border * 2,notificationFontManager.getFont().getSize() + border * 2), new Color(150, 150, 150, (int)Math.clamp(notification.fade,0,175)),30,true,2).draw();
            notificationFontManager.drawStaticString(scene, notification.text, pos, notification.getFadeColor());

            y += notificationFontManager.getFont().getSize() + border * 2;

            notification.updateFadeOut();
            queueNotificationRemoval(notification);
        }
    }

    private void queueNotificationRemoval(Notification notification) {
        if (notification.fade <= 0) queuedNotificationsRemoval.add(notification);
    }

    private void cycleQueuedItems() {
        int removals = Math.clamp(notifications.size() - maximumNotifications,0, notifications.size());
        for (int i = 0; i < removals; i++) queuedNotificationsRemoval.add(notifications.get(i));
        notifications.removeAll(queuedNotificationsRemoval);
    }


    public void sendNotification(String text, Color color, float fadeSpeed) {
        notifications.add(new Notification(text, color, fadeSpeed));
    }

    public void sendNotification(String text, Color color) {
        sendNotification(text,color,1);
    }


    public void setMaximumNotifications(int maximumNotifications) {
        this.maximumNotifications = maximumNotifications;
    }

    public void setNotificationFont(Font font) {
        this.notificationFontManager = new FontManager(font);
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
