package com.ejo.ui;

import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.ThreadUtil;
import com.ejo.util.setting.Container;
import com.ejo.util.time.TickRateLogger;
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

    private Scene scene;

    private Vector mousePos;


    private String title;
    private Vector pos;
    private Vector size;

    private boolean open;


    private int maxFPS;
    private int maxTPS;

    private boolean vSync;
    private int antiAliasingLevel;
    private double uiScale;


    //TODO: Maybe bring back the maintenance thread in order to have the loggers work. Or find another workaround
    private final TickRateLogger fpsLogger;
    private final TickRateLogger tpsLogger;

    private DebugMode debugMode;
    private PerformanceMode performanceMode;


    public Window(String title, Vector size, Scene startingScene) {
        this.title = title;
        this.pos = new Vector(100,100); //TODO: Make this position default to the center of the screen
        this.size = size;

        this.maxFPS = 60;
        this.maxTPS = 60;

        this.vSync = true;
        this.antiAliasingLevel = 0;

        this.uiScale = 1;

        this.debugMode = DebugMode.OFF;
        this.performanceMode = PerformanceMode.STANDARD;

        this.fpsLogger = new TickRateLogger();
        this.tpsLogger = new TickRateLogger();

        this.scene = startingScene;
        this.open = false;
    }

    // =================================================

    // INITIALIZE FUNCTIONS

    // =================================================

    public Window init() {
        final long NULL = 0L;
        if (!glfwInit()) throw new IllegalStateException("Failed to init GLFW");

        //Creating the window
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        windowId = glfwCreateWindow((int) size.getX(), (int) size.getY(), title, NULL, NULL);
        if (windowId == NULL) throw new IllegalStateException("Window could not be created");

        //Sets the Input Callbacks
        initInputCallbacks();

        // Load the window icon
        initWindowIcon(getClass().getResource("/icon.png"));

        //Creating the monitor
        glfwGetVideoMode(glfwGetPrimaryMonitor());

        //Show the window
        setPos(pos);
        glfwShowWindow(windowId);

        //Set performance attributes
        setAntiAliasingLevel(antiAliasingLevel);
        setVSync(vSync);

        //Sets the window context to display graphics
        glfwMakeContextCurrent(windowId);
        GL.createCapabilities();
        GL11.glClearColor(0f, 0f, 0f, 0f);

        //If all is successfully set, open the window.
        this.open = true;

        return this;
    }

    private void initInputCallbacks() {
        glfwSetKeyCallback(windowId, (window, key, scancode, action, mods) -> {
            scene.onKeyPress(key, scancode, action, mods);
            //TODO: Put Key.java interface in here
        });
        glfwSetMouseButtonCallback(windowId, (window, button, action, mods) -> {
            scene.onMouseClick(button, action, mods, getMousePos());
            //TODO: Put Mouse.java interface in here
        });
        glfwSetScrollCallback(windowId, (window, scrollX, scrollY) -> {
            scene.onMouseScroll((int) scrollY, getMousePos());
        });
    }

    private void initWindowIcon(URL imageURL) {
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

    // =================================================

    // STEP FORWARD FUNCTIONS

    // =================================================


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

        //Update Draw Scale before drawing elements
        GLManager.scale(uiScale);
        GLManager.textureScale(uiScale);

        //Draw all scene elements
        scene.draw();

        //Draw debug menu on top
        switch (debugMode) {
            case DEBUG_SIMPLE -> drawSimpleDebugMenu();
            case DEBUG_ADVANCED -> drawAdvancedDebugMenu();
        }

        //Finish Drawing here
        glfwSwapBuffers(windowId);

        //Update screen's elements based on performance mode
        switch (performanceMode) {
            case STANDARD -> GLFW.glfwPollEvents();
            case ECONOMIC -> GLFW.glfwWaitEvents();
        }
    }

    private void drawSimpleDebugMenu() {

    }

    private void drawAdvancedDebugMenu() {

    }

    private void tick() {
        updateMousePos();
        scene.tick();
    }

    // =================================================

    // LOOP FUNCTIONS

    // =================================================

    public void runMainRenderLoop() {
        Runnable renderItems = () -> {
            this.open = !glfwWindowShouldClose(windowId);
            updateWindowPosSize(); //I put update window in the draw loop instead of tick because it's the main thread
            draw();
            fpsLogger.tick();
        };
        if (vSync) {
            while (open) renderItems.run();
        } else {
            limitedRateLoop(renderItems,maxFPS);
        }
        close();
    }

    //MUST be run first in order to start the loop. If Draw is run first, the window will not start
    public void startThreadTickLoop() {
        Thread thread = new Thread(() -> {
            Runnable tickItems = () -> {
                tick();
                tpsLogger.tick();
            };
            limitedRateLoop(tickItems,maxTPS);
        });
        thread.setName("Tick Thread");
        thread.start();
    }

    private void limitedRateLoop(Runnable runnable, int maxTickRate) {
        //We could not rout open through the method -> it would pass by value and wouldn't update inside the loop
        while (open) {
            long startTimeNS = System.nanoTime();
            runnable.run();
            long endTimeNS = System.nanoTime();
            long tickTimeNS = endTimeNS - startTimeNS;
            long sleepTimeNS = (1000000000 / maxTickRate - tickTimeNS);
            if (sleepTimeNS > 0) ThreadUtil.sleepThread(sleepTimeNS / 1000000);
        }
    }

    // =================================================

    // UPDATE WINDOW FUNCTIONS

    // =================================================

    private void updateWindowPosSize() {
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

    private void updateMousePos() {
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(windowId, buffer, null);
        double mouseX = buffer.get(0);
        glfwGetCursorPos(windowId, null, buffer);
        double mouseY = buffer.get(0);
        this.mousePos = new Vector(mouseX, mouseY);
    }

    //TODO: Test and properly implement this. Have when called force close the window. It currently doesn't
    public void close() {
        GLFW.glfwDestroyWindow(windowId);
        GLFW.glfwTerminate();
    }

    // =================================================

    // SETTER FUNCTIONS

    // =================================================

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
        glfwSetWindowPos(windowId, pos.getXi(), pos.getYi());
        this.pos = pos;
        return this;
    }

    public Window setSize(Vector size) {
        glfwSetWindowSize(windowId, size.getXi(), size.getYi());
        this.size = size;
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

    public Window setUIScale(double uiScale) {
        this.uiScale = uiScale;
        return this;
    }

    public Window setVSync(boolean vSync) {
        glfwSwapInterval(vSync ? GLFW_TRUE : GLFW_FALSE);
        this.vSync = vSync;
        return this;
    }

    public Window setAntiAliasingLevel(int level) {
        if (antiAliasingLevel > 0) GLFW.glfwWindowHint(GLFW_SAMPLES, level);
        this.antiAliasingLevel = level;
        return this;
    }

    public void setDebugMode(DebugMode mode) {
        this.debugMode = mode;
    }

    public void setPerformanceMode(PerformanceMode mode) {
        this.performanceMode = mode;
    }

    // =================================================

    // GETTER FUNCTIONS

    // =================================================

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
        return size.getMultiplied(1/uiScale);
    }

    public Vector getMousePos() {
        return mousePos.getMultiplied(1/uiScale);
    }

    public float getTPS() {
        return Math.round(tpsLogger.getTickRate());
    }

    public float getFPS() {
        return Math.round(fpsLogger.getTickRate());
    }

    public DebugMode getDebugMode() {
        return debugMode;
    }

    public PerformanceMode getPerformanceMode() {
        return performanceMode;
    }

    // =================================================

    // MODE ENUMS

    // =================================================

    //These modes are split into enums in hopes of potentially adding multiple different future modes
    public enum DebugMode {
        OFF,
        DEBUG_SIMPLE, //Make a mini debug mode
        DEBUG_ADVANCED //Make a hardcore higher end debug mode
    }

    public enum PerformanceMode {
        STANDARD,
        ECONOMIC
    }
}