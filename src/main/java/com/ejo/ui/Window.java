package com.ejo.ui;

import com.ejo.ui.scene.Scene;
import com.ejo.util.action.OnChange;
import com.ejo.util.math.Vector;
import com.ejo.util.time.StopWatch;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.net.URL;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

//https://www.glfw.org/docs/latest/window.html#window_refresh
/**
 * The Window class is a container class for the LWJGL3 WindowID. It incorporates GLFW methods in an easy-to-use object-oriented
 * fashion for the window.
 */
public class Window {

    private long windowId;

    private String title;
    private Vector pos;
    private Vector size;

    private int maxFPS;
    private int maxTPS;

    private int frames;
    private int ticks;

    private int fps;
    private int tps;

    private boolean vSync;

    private int antiAliasingLevel;

    private double uiScale;

    private boolean economic;

    private boolean debug;


    private boolean open;

    private Vector mousePos;


    private Scene scene;


    public Window(String title, Vector pos, Vector size, Scene startingScene) {
        this.title = title;
        this.pos = pos;
        this.size = size;

        this.maxFPS = 60;
        this.maxTPS = 60;

        this.vSync = true;
        this.antiAliasingLevel = 0;

        this.uiScale = 1;

        this.economic = false;
        this.debug = false;

        this.scene = startingScene;
    }

    public Window init() {
        final long NULL = 0L;
        if (!glfwInit()) throw new IllegalStateException("Failed to init GLFW");

        setAntiAliasingLevel(antiAliasingLevel);

        //Creating the window
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        windowId = glfwCreateWindow((int) size.getX(), (int) size.getY(), title, NULL, NULL);
        if (windowId == NULL) throw new IllegalStateException("Window could not be created");

        //Creating the monitor
        glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Load the window icon
        registerWindowIcon(getClass().getResource("/icon.png"));

        //Show the window
        setPos(pos);
        glfwShowWindow(windowId);

        //Sets the window context to display graphics
        glfwMakeContextCurrent(windowId);
        GL.createCapabilities();
        GL11.glClearColor(0f, 0f, 0f, 0f);

        setVSync(vSync);

        setScene(scene);

        this.open = true;

        return this;
    }

    private void draw() {
        GL.createCapabilities();
        GL11.glViewport(0, 0, (int) size.getX(), (int) size.getY());
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, size.getX(), size.getY(), 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GLManager.scale(uiScale); //Shape Scale
        GLManager.textureScale(uiScale); //Texture Scale

        scene.draw(); //Draw the screen

        glfwSwapBuffers(windowId); //Finish Drawing here

        if (economic) {
            try {
                GLFW.glfwWaitEvents();
            } catch (NullPointerException e) {
                GLFW.glfwPollEvents();
            }
        } else {
            try {
                GLFW.glfwPollEvents();
            } catch (NullPointerException e) {
                GLFW.glfwWaitEvents();
            }
        }
    }

    private void tick() {
        onKeyPress();
        onMouseClick();
        onMouseScroll();
        scene.tick();
    }

    private void onKeyPress() {
        GLFWKeyCallback callback = glfwSetKeyCallback(windowId, (window, key, scancode, action, mods) -> {
            scene.onKeyPress(key, scancode, action, mods);
        });
        closeAutoClosable(callback);
    }

    private void onMouseClick() {
        GLFWMouseButtonCallback callback = glfwSetMouseButtonCallback(windowId, (window, button, action, mods) -> {
            scene.onMouseClick(button, action, mods, getScaledMousePos());
        });
        closeAutoClosable(callback);
    }

    private void onMouseScroll() {
        GLFWScrollCallback callback = glfwSetScrollCallback(windowId, (window, scrollX, scrollY) -> {
            scene.onMouseScroll((int)scrollY, getScaledMousePos());
        });
        closeAutoClosable(callback);
    }

    private void runRenderLoop() {
        while (open) {
            this.open = !glfwWindowShouldClose(windowId);
            long startTimeNS = System.nanoTime();
            updateWindow();
            draw();
            frames++;
            long endTimeNS = System.nanoTime();

            long tickTimeNS = endTimeNS - startTimeNS;
            long sleepTimeNS = (1000000000 / maxFPS - tickTimeNS);
            if (!vSync) if (sleepTimeNS > 0) sleepThread(sleepTimeNS / 1000000);
        }
    }

    private void startTickLoopThread() {
        Thread thread = new Thread(() -> {
            while (open) {
                long startTimeNS = System.nanoTime();
                updateMousePos();
                tick();
                ticks++;
                long endTimeNS = System.nanoTime();

                long tickTimeNS = endTimeNS - startTimeNS;
                long sleepTimeNS = 1000000000 / maxTPS - tickTimeNS;
                if (sleepTimeNS > 0) sleepThread(sleepTimeNS / 1000000);
            }
        });
        thread.setName("Tick Thread");
        thread.start();
    }

    private void updateWindow() {
        IntBuffer buffer = BufferUtils.createIntBuffer(1);
        glfwGetWindowPos(windowId, buffer, null);
        double x = buffer.get(0);
        glfwGetWindowPos(windowId, null, buffer);
        double y = buffer.get(0);
        Vector pos = new Vector(x, y);
        setPos(pos);

        glfwGetWindowSize(windowId, buffer, null);
        double w = buffer.get(0);
        glfwGetWindowSize(windowId, null, buffer);
        double h = buffer.get(0);
        Vector size = new Vector(w, h);
        if (size.getMagnitude() != 0) setSize(size);
    }

    private Vector updateMousePos() {
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(windowId, buffer, null);
        double mouseX = buffer.get(0);
        glfwGetCursorPos(windowId, null, buffer);
        double mouseY = buffer.get(0);
        return mousePos = new Vector(mouseX, mouseY);
    }

    private void registerWindowIcon(URL imageURL) {
        if (imageURL != null) {
            int width = 512;
            int height = 512;
            GLFWImage glfwImage = GLFWImage.malloc();
            GLFWImage.Buffer glfwImageBuffer = GLFWImage.malloc(1);
            //glfwImage.set(width, height, TextureUtil.getImageBuffer(imageURL, width, height));
            glfwImageBuffer.close();
            glfwSetWindowIcon(windowId, glfwImageBuffer.put(0, glfwImage));
        }
    }

    private void sleepThread(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeAutoClosable(AutoCloseable closable) {
        try {
            closable.close();
        } catch (Exception ignored) {
        }
    }




    private void calculateFPSTPS(StopWatch stopWatch) {
        stopWatch.start();
        if (stopWatch.hasTimePassedS(1)) { //TPS-FPS Updater
            fps = frames;
            frames = 0;
            tps = ticks;
            ticks = 0;
            stopWatch.restart();
        }
    }


    public Window run() {
        init();
        startTickLoopThread();
        runRenderLoop();
        return this;
    }

    public Window close() {
        GLFW.glfwDestroyWindow(windowId);
        GLFW.glfwTerminate();
        return this;
    }


    public Window setScene(Scene scene) {
        this.scene = scene;
        return this;
    }

    public Window setTitle(String title) {
        glfwSetWindowTitle(windowId, title);
        this.title = title;
        return this;
    }

    public Window setPos(Vector pos) {
        glfwSetWindowPos(windowId, (int) pos.getX(), (int) pos.getY());
        this.pos = pos;
        return this;
    }

    public Window setSize(Vector size) {
        glfwSetWindowSize(windowId, (int) size.getX(), (int) size.getY());
        this.size = size;
        return this;
    }

    public Window setVSync(boolean vSync) {
        glfwSwapInterval(vSync ? 1 : 0);
        this.vSync = vSync;
        return this;
    }

    public Window setAntiAliasingLevel(int level) {
        if (antiAliasingLevel > 0) GLFW.glfwWindowHint(GLFW_SAMPLES, level);
        this.antiAliasingLevel = level;
        return this;
    }

    public Window setUIScale(double uiScale) {
        this.uiScale = uiScale;
        return this;
    }

    public Window setEconomic(boolean economic) {
        this.economic = economic;
        return this;
    }

    public Window setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public Window setMaxTPS(int maxTPS) {
        this.maxTPS = maxTPS;
        return this;
    }

    public Window setMaxFPS(int maxFPS) {
        this.maxFPS = maxFPS;
        return this;
    }


    public long getWindowId() {
        return windowId;
    }

    public Scene getScene() {
        return scene;
    }

    public String getTitle() {
        return title;
    }

    public Vector getPos() {
        return pos;
    }

    public Vector getSize() {
        return size;
    }

    public Vector getScaledSize() {
        return size.getMultiplied(1/uiScale);
    }

    public Vector getMousePos() {
        return mousePos;
    }

    public Vector getScaledMousePos() {
        return mousePos.getMultiplied(1/uiScale);
    }

    public boolean isDebug() {
        return debug;
    }

    public int getTPS() {
        return tps;
    }

    public int getFPS() {
        return fps;
    }

}