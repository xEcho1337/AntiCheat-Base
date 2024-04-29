package net.echo.anticheatbase.tasks.model;

public abstract class AbstractTask implements Runnable {

    private long lastExecuteTime;

    public void update() {
        lastExecuteTime = System.currentTimeMillis();
    }

    public long getDifference() {
        return System.currentTimeMillis() - lastExecuteTime;
    }
}
