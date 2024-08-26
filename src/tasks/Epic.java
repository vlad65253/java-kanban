package tasks;

import manager.TypeTask;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> idSubTask = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, Integer idEpic) {
        super(name, description);
        setId(idEpic);
    }

    public Epic(Integer id, String name, String description, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
        this.typeTask = TypeTask.EPIC;
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
}
