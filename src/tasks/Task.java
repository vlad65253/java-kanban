package tasks;

import manager.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String description;
    private int id;
    private String name;
    private TaskStatus status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String description, String name) {
        this.description = description;
        this.name = name;
        status = TaskStatus.NEW;
    }

    public Task(String description, String name, TaskStatus status) {
        this.description = description;
        this.name = name;
        this.status = status;
    }

    public Task(int id, String name, TaskStatus status, String description, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (duration == null) {
            return startTime;
        }
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TypeTask getType() {
        return TypeTask.TASK;
    }

    @Override
    public String toString() {
        return "Task {" +
                "Описание ='" + description + '\'' +
                ", Айди задачи =" + id +
                ", Название ='" + name + '\'' +
                ", Статус выполнения: " + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}