package com.ejo.ui;

import com.ejo.ui.render.GLUtil;
import com.ejo.util.misc.ImageUtil;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.ThreadUtil;
import com.ejo.util.time.TickRateLogger;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.awt.*;
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


    private final TickRateLogger fpsLogger;
    private final TickRateLogger tpsLogger;

    private DebugMode debugMode;
    private PerformanceMode performanceMode;


    public Window(String title, Vector size, Scene startingScene) {
        this.title = title;
        this.size = size;
        this.scene = startingScene;

        this.mousePos = Vector.NULL();
        this.open = false;

        this.maxFPS = 60;
        this.maxTPS = 60;

        this.vSync = false; //Once the window starts, VSync cannot be changed for some reason... TODO: Look into this
        this.antiAliasingLevel = 0;//Once the window starts, Aliasing cannot be changed either

        this.uiScale = 1;

        this.debugMode = DebugMode.OFF;
        this.performanceMode = PerformanceMode.STANDARD;

        this.fpsLogger = new TickRateLogger(.25f, 20);
        this.tpsLogger = new TickRateLogger(.25f,20);
    }

    // =================================================

    // INITIALIZE FUNCTIONS

    // =================================================

    public Window init() {
        final long NULL = 0L;
        if (!glfwInit()) throw new IllegalStateException("Failed to init GLFW");

        //Initialize the window's on-screen position
        initWindowPosition();

        //Set AntiAliasing Level (Must be set before window creation)
        GLFW.glfwWindowHint(GLFW_SAMPLES, antiAliasingLevel);

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

        //Sets the window context to display graphics
        glfwMakeContextCurrent(windowId);
        GL.createCapabilities();

        //Setup VSync
        setVSync(vSync);

        //Initialize the window & attach it to the scene
        this.scene.initWindow(this);

        //If all is successfully set, open the window.
        this.open = true;

        return this;
    }

    private void initWindowPosition() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        double width = gd.getDisplayMode().getWidth();
        double height = gd.getDisplayMode().getHeight();
        this.pos = new Vector(width / 2 - size.getX() / 2,height / 2 - size.getY() / 2);
    }

    private void initInputCallbacks() {
        glfwSetKeyCallback(windowId, (window, key, scancode, action, mods) -> {
            scene.onKeyPress(key, scancode, action, mods);
            for (int i = scene.getSceneManagers().size() - 1; i >= 0 ; i--)
                scene.getSceneManagers().get(i).onKeyPress(key, scancode, action, mods);
            //TODO: Put Key.java interface in here
        });
        glfwSetMouseButtonCallback(windowId, (window, button, action, mods) -> {
            scene.onMouseClick(button, action, mods, getMousePos());
            for (int i = scene.getSceneManagers().size() - 1; i >= 0 ; i--)
                scene.getSceneManagers().get(i).onMouseClick(button, action, mods, getMousePos());
            //TODO: Put Mouse.java interface in here
        });
        glfwSetScrollCallback(windowId, (window, scrollX, scrollY) -> {
            scene.onMouseScroll(scrollY, getMousePos());
            for (int i = scene.getSceneManagers().size() - 1; i >= 0 ; i--)
                scene.getSceneManagers().get(i).onMouseScroll(scrollY, getMousePos());
        });
    }

    private void initWindowIcon(URL imageURL) {
        if (imageURL != null) {
            int width = 512;
            int height = 512;
            GLFWImage glfwImage = GLFWImage.malloc();
            GLFWImage.Buffer glfwImageBuffer = GLFWImage.malloc(1);
            glfwImage.set(width, height, ImageUtil.getByteBuffer(ImageUtil.getBufferedImage(width, height, imageURL)));
            glfwImageBuffer.close();
            glfwSetWindowIcon(windowId, glfwImageBuffer.put(0, glfwImage));
        }
    }

    //Think about putting this in the constructor as it can only be set at the start
    public Window initAntiAliasingLevel(int level) {
        this.antiAliasingLevel = level;
        return this;
    }

    // =================================================

    // STEP FORWARD FUNCTIONS

    // =================================================


    private void draw() {
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
        GLUtil.scale(uiScale);
        GLUtil.textureScale(uiScale);

        //Draw all scene elements
        scene.draw();

        //Draw all scene managers on top of the scene draw method
        for (int i = scene.getSceneManagers().size() - 1; i >= 0 ; i--)
            scene.getSceneManagers().get(i).draw(getMousePos());

        //Finish Drawing here
        glfwSwapBuffers(windowId);

        //Update screen's elements based on performance mode
        switch (performanceMode) {
            case STANDARD -> GLFW.glfwPollEvents();
            case ECONOMIC -> GLFW.glfwWaitEvents();
        }
    }

    private void tick() {
        updateWindowPosSize();
        updateMousePos();
        scene.tick();
        scene.updateMouseHovered(); //Maybe think about having this in the render loop?

        //Tick all scene managers
        for (int i = scene.getSceneManagers().size() - 1; i >= 0 ; i--)
            scene.getSceneManagers().get(i).tick(getMousePos());
    }

    // =================================================

    // LOOP FUNCTIONS

    // =================================================

    public void runMainRenderLoop() {
        Runnable renderItems = () -> {
            this.open = !glfwWindowShouldClose(windowId); //Update if the window is open constantly in here
            scene.updateAnimation(); //Maybe Move this to a separate thread??
            draw();
            fpsLogger.updateTickRate();
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
                tpsLogger.updateTickRate();
                tpsLogger.tick();
            };
            limitedRateLoop(tickItems,maxTPS);
        });
        thread.setName("Tick Thread");
        thread.start();
    }

    private void limitedRateLoop(Runnable runnable, int maxTickRate) {
        //We could not rout open through the method -> it would pass by value and wouldn't update inside the loop
        // A workaround could be to use a container, but that's too ugly, so we'll keep the method non-static
        //TODO: Reformat this limited loop. At higher maxTickRates, the loop is too slow and fails
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
        this.pos = new Vector(x, y);

        glfwGetWindowSize(windowId, buffer, null);
        double w = buffer.get(0);
        glfwGetWindowSize(windowId, null, buffer);
        double h = buffer.get(0);
        Vector size = new Vector(w, h);
        if (size.getMagnitude() != 0) this.size = size;
    }

    private void updateMousePos() {
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(windowId, buffer, null);
        double mouseX = buffer.get(0);
        glfwGetCursorPos(windowId, null, buffer);
        double mouseY = buffer.get(0);
        this.mousePos = new Vector(mouseX, mouseY);
    }

    public void close() {
        this.open = false;
        GLFW.glfwDestroyWindow(windowId);
        GLFW.glfwTerminate();
    }

    // =================================================

    // SETTER FUNCTIONS

    // =================================================

    public Window setScene(Scene scene) {
        this.scene = scene;
        this.scene.initWindow(this);
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

    public int getTPS() {
        return (int)tpsLogger.getTickRate();
    }

    public int getFPS() {
        return (int)fpsLogger.getTickRate();
    }

    public int getMaxFPS() {
        return maxFPS;
    }

    public int getMaxTPS() {
        return maxTPS;
    }

    public boolean isVSync() {
        return vSync;
    }

    public double getUiScale() {
        return uiScale;
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