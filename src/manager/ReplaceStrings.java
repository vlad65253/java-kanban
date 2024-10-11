package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static manager.TypeTask.*;

public class ReplaceStrings {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    static DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    static DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

    public static Task fromString(String str) {

        String[] split = str.split(",");
        int id = Integer.parseInt(split[0]);
        TypeTask typeTask = TypeTask.valueOf(split[1]);
        String name = split[2];
        TaskStatus taskStatus = TaskStatus.valueOf(split[3]);
        String description = split[4];
        LocalDateTime startTime = LocalDateTime.parse(split[5], formatter);
        Duration duration = Duration.ofMinutes(Long.parseLong(split[7]));

        if (typeTask.equals(EPIC)) {
            LocalDateTime epicEndTime = LocalDateTime.parse(split[6], formatter);
            return new Epic(id, name, taskStatus, description, startTime, epicEndTime, duration);
        } else if (typeTask.equals(SUBTASK)) {
            int epicId = Integer.parseInt(split[8]);
            return new SubTask(id, name, taskStatus, description, startTime, duration, epicId);
        } else {
            return new Task(id, name, taskStatus, description, startTime, duration);
        }
    }

    private static String getEpicIdForSubtask(Task task) {
        if (task.getTypeTask().equals(SUBTASK)) {
            return Integer.toString(((SubTask) task).getIdMain());
        }
        return "";
    }

    protected static String toString(Task task, TypeTask typeTask) {
        String startTime = task.getStartTime() != null ? task.getStartTime().format(formatter) : "";
        String endTime = task.getEndTime() != null ? task.getEndTime().format(formatter) : "";
        String duration = task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "";

        return task.getId() + "," +
                task.getTypeTask() + "," +
                task.getName() + "," +
                task.getTaskStatus() + "," +
                task.getDescription() + "," +
                startTime + "," +
                endTime + "," +
                duration + "," +
                getEpicIdForSubtask(task);

    }

}
