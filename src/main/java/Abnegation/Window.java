package Abnegation;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    private float r,g,b,a;
    private boolean fadeToBlack = false;
    private static Window window = null;
    private Window(){
        this.width=1920;
        this.height=1080;
        this.title="Hello world!";
        r=1;
        g=1;
        b=1;
        a=1;
    }

    public static Window get(){
        //this makes that the only time the window could be created is the first time we call window.get, so there'll only be one window
        if (Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run(){
        System.out.println("LWJGL > "+ Version.getVersion() + " :)");

        init();
        loop();



        //free the memory so it's clean and proper
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        // terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){
        //setup an error callback, this is to say where to show errors, this is the same as System.err.println("errooor")
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW, glfwInit() return true if it succeed to initialize and false in contrary
        if (!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW. :(");
        }

        //Configure GLFW
        glfwDefaultWindowHints(); // hints are like, do you want it to be resizable...
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // so that it's not visible until we're done creating the window
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Create the window, it will use upper hints to create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL); //returns a long, memory address, a handler
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GLFW window :(");
        }

        //call it our function whenever there is mouse callback
        // :: is a lambda function, like forward this to the function x > f(x)
        //we can see on the website how to call those functions (glfw.org/docs/)
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //Make the Opengl context current, make the window current
        glfwMakeContextCurrent(glfwWindow);
        //Enable V-sync, this number is how long we wait, so here as fast as we can
        glfwSwapInterval(1);

        //Make the window
        glfwShowWindow(glfwWindow);


        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();


    }
    public void loop(){
        //when frame started and ended
        float beginTime = Time.getTime();
        float endTime = Time.getTime();

        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll events, key events, mouse events
            glfwPollEvents();

            glClearColor(r, g, b, a); //import GL11 without the c
            glClear(GL_COLOR_BUFFER_BIT);//Tells openGl how to clear the buffer, flush what's above to the entire screen
            if(fadeToBlack){
                r = Math.max(r - 0.01f, 0);
                g = Math.max(g - 0.01f, 0);
                b = Math.max(b - 0.01f, 0);

            }

            if(KeyListener.isKeyPressed(GLFW_KEY_SPACE)){
                fadeToBlack = true;
                System.out.println("IT S PRESSEEEEEEDD");
            }

            glfwSwapBuffers(glfwWindow);

            //dt means delta time, time elapsed doing the frame
            endTime = Time.getTime();
            float dt = endTime - beginTime;
            beginTime = endTime;
            //getting begin time here is making sure that we also count any interuption or things like that
        }

    }
}
