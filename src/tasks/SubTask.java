package tasks;

import manager.TypeTask;

public class SubTask extends Task {
    private int idMain;
    private TypeTask typeTask = TypeTask.SUBTASK;

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
}
