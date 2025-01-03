package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class ReplaceStrings {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    protected static Task fromString(String value) {
        String[] arrays = value.split(",");
        int id = Integer.parseInt(arrays[0]);
        String typeTask = arrays[1];
        String name = arrays[2];
        TaskStatus status = TaskStatus.valueOf(arrays[3]);
        String description = arrays[4];
        LocalDateTime startTime = LocalDateTime.parse(arrays[5], formatter);
        Duration duration = Duration.ofMinutes(Long.parseLong(arrays[7]));

        if (typeTask.equals("EPIC")) {
            LocalDateTime epicEndTime = LocalDateTime.parse(arrays[6], formatter);
            return new Epic(id, name, status, description, startTime, epicEndTime, duration);
        } else if (typeTask.equals("SUBTASK")) {
            int epicId = Integer.parseInt(arrays[8]);
            return new SubTask(id, name, status, description, startTime, duration, epicId);
        } else {
            return new Task(id, name, status, description, startTime, duration);
        }
    }

    private static String getEpicIdForSubtask(Task task) {
        if (task.getType().equals(TypeTask.SUBTASK)) {
            return Integer.toString(((SubTask) task).getEpicId());
        }
        return "";
    }

    protected static String toString(Task task) {

        String startTime = task.getStartTime() != null ? task.getStartTime().format(formatter) : "";
        String endTime = task.getEndTime() != null ? task.getEndTime().format(formatter) : "";
        String duration = task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "";

        return task.getId() + "," +
                task.getType() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                startTime + "," +
                endTime + "," +
                duration + "," +
                getEpicIdForSubtask(task);
    }
}