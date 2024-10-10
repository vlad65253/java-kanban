package tasks;

import manager.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private Integer id;
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private Duration duration;
    LocalDateTime startTime;

    public Task(String description, String name) {
        this.description = description;
        this.name = name;
        taskStatus = TaskStatus.NEW;
    }

    public Task(String description, String name, TaskStatus taskStatus) {
        this.description = description;
        this.name = name;
        this.taskStatus = taskStatus;
    }

    public Task(int id, String name, TaskStatus taskStatus, String description, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.taskStatus = taskStatus;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(id, otherTask.id) && Objects.equals(name, otherTask.name) && Objects.equals(description, otherTask.description);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (id != null) {
            hash = id.hashCode();
        }
        if (name != null) {
            hash = hash + name.hashCode();
        }
        if (description != null) {
            hash = hash + description.hashCode();
        }
        return hash;
    }

    public TypeTask getTypeTask() {
        return TypeTask.TASK;
    }
}
