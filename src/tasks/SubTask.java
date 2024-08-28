package tasks;

import manager.TypeTask;

public class SubTask extends Task {
    private final int idMain;

    public SubTask(String name, String description, Integer idMain, TaskStatus taskStatus) {
        super(name, description, taskStatus);
        this.idMain = idMain;
    }

    public SubTask(Integer id, String name, String description, Integer idMain, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
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
