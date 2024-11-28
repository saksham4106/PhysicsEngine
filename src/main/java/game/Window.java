package game;

import audio.AudioContext;
import callback.KeyEventListener;
import callback.MouseEventListener;
import events.EventBus;
import events.WindowCloseEvent;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scenes.Scene;
import scenes.StartingMenuScene;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    public static int width, height;
    private final String title;
    private long window;
    public static Logger LOGGER = LoggerFactory.getLogger(Window.class);
    private static Scene currentScene;

    public static AudioContext audioContext = new AudioContext();

    private boolean isResized = true;

    public Window() {
        width = 1366;
        height = 706;
        this.title = "PokeGL";
    }

    public void run(){
        init();
        loop();

        audioContext.destroy();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) LOGGER.error("Failed to initialize GLFW");


        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
//        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        window = glfwCreateWindow(width, height, this.title, NULL, NULL);
        if (window == NULL) {
            LOGGER.error("Failed to create GLFW window");
        }

        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(0);

        glfwShowWindow(window);

        glfwSetKeyCallback(window, KeyEventListener::keyCallback);
        glfwSetMouseButtonCallback(window, MouseEventListener::mouseButtonCallback);
        glfwSetScrollCallback(window, MouseEventListener::mouseScrollCallback);
        glfwSetCursorPosCallback(window, MouseEventListener::mousePosCallback);
        glfwSetCharCallback(window, KeyEventListener::charCallback);

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            Window.width = width;
            Window.height = height;
            this.isResized = true;
        });



        audioContext.initialise();
        GL.createCapabilities();

        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        currentScene = new StartingMenuScene();
        currentScene.init();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static Scene getCurrentScene(){
        return currentScene;
    }




    private void loop() {

        float beginTime = (float) glfwGetTime();

        float endTime;
        float dt = -1.0f;
        float buffer = 0f;

        while (!glfwWindowShouldClose(window)) {

            if (buffer >= 1 / 60f) {
                currentScene.tick(buffer);
                buffer = 0;
            }

            glfwPollEvents();

            glClearColor(0, 0, 0, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                if (isResized) {
                    glViewport(0, 0, width, height);
                    isResized = false;
                }
                currentScene.update(dt);

            }

            glfwSwapBuffers(window);

            endTime = (float) glfwGetTime();

            dt = endTime - beginTime;
            beginTime = endTime;
            buffer += dt;
        }
        EventBus.invoke(new WindowCloseEvent());
//        currentScene.save();
//        currentScene.destroy();
    }
}
