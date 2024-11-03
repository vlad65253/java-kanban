package tasks;

import manager.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private final int epicId;

    public SubTask(String description, String name, int epicId) {
        super(description, name);
        this.epicId = epicId;
    }

    public SubTask(String description, String name, TaskStatus status, int epicId) {
        super(description, name, status);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, TaskStatus status, String description, LocalDateTime startTime,
                   Duration duration, int epicId) {
        super(id, name, status, description, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TypeTask getType() {
        return TypeTask.SUBTASK;
    }

    @Override
    public String toString() {
        return "SubTask {" +
                "Описание ='" + getDescription() + '\'' +
                ", Айди задачи =" + getId() +
                ", Название ='" + getName() + '\'' +
                ", Статус выполнения: " + getStatus() +
                '}';
    }
}