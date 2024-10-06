package tasks;

import manager.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> idSubTask = new ArrayList<>();
    LocalDateTime endTime;

    public Epic(String description, String name) {
        super(description, name);
    }

    public Epic(int id, String name, TaskStatus taskStatus, String description, LocalDateTime startTime,
                LocalDateTime endTime, Duration duration) {
        super(id, name, taskStatus, description, startTime, duration);
        this.endTime = endTime;
    }

    public Epic(String description, String name, TaskStatus status) {
        super(description, name, status);
    }

    public ArrayList<Integer> getIdSubTask() {
        return idSubTask;
    }

    public void delIdSubTask(Integer id) {
        idSubTask.remove(id);
    }

    public void addIdSubTask(Integer idSub) {
        idSubTask.add(idSub);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.EPIC;
    }
}
