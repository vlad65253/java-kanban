package tasks;

import manager.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final int idMain;

    public SubTask(String description, String name, int idMain) {
        super(description, name);
        this.idMain = idMain;
    }

    public SubTask(String description, String name, TaskStatus status, int idMain) {
        super(description, name, status);
        this.idMain = idMain;
    }

    public SubTask(int id, String name, TaskStatus status, String description, LocalDateTime startTime,
                   Duration duration, int idMain) {
        super(id, name, status, description, startTime, duration);
        this.idMain = idMain;
    }

    public int getIdMain() {
        return idMain;
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.SUBTASK;
    }
}
