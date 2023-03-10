package util;

public class Time {
    public static float timeStarted = System.nanoTime();

    public static float getTime(){
        //1e-9 is to convert nanoseconds in seconds
        return (float)((System.nanoTime() - timeStarted) * 1E-9);
    }
}
