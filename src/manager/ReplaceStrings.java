package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

public class ReplaceStrings {
    public static Task fromString(String str) {
        String[] split = str.split(",");
        int id = Integer.parseInt(split[0]);
        String typeTask = split[1];
        String name = split[2];
        TaskStatus status = TaskStatus.valueOf(split[3]);
        String description = split[4];
        if (typeTask.equals("EPIC")) {
            return new Epic(id, name, description, status);
        } else if (typeTask.equals("SUBTASK")) {
            int epicId = Integer.parseInt(split[5]);
            return new SubTask(id, name, description, epicId, status);
        } else {
            return new Task(id, name, description, status);

        }
    }

    private static String getEpicIdForSubtask(Task task) {
        if (task.getTypeTask().equals(TypeTask.SUBTASK)) {
            return Integer.toString(((SubTask) task).getIdMain());
        }
        return "";
    }

    protected static String toString(Task task, TypeTask typeTask) {

        return task.getId() + "," +
                typeTask + "," +
                task.getName() + "," +
                task.getTaskStatus() + "," +
                task.getDescription() + "," +
                getEpicIdForSubtask(task);

    }

}
