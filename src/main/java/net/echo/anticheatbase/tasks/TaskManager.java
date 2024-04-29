package net.echo.anticheatbase.tasks;

import net.echo.anticheatbase.AntiCheat;
import net.echo.anticheatbase.tasks.impl.PingSendTask;
import net.echo.anticheatbase.tasks.model.AbstractTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskManager {

    // Task, delay
    private final Map<AbstractTask, Long> tasks = new ConcurrentHashMap<>();

    public TaskManager(AntiCheat antiCheat) {
        tasks.put(new PingSendTask(antiCheat), 80L);
    }

    public void onTick() {
        tasks.forEach((task, delay) -> {
            long difference = task.getDifference();

            if (difference < delay) return;

            task.update();
            task.run();
        });
    }
}
