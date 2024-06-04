package tasks;

public class SubTask extends Task {
    private int idMain;

    public SubTask(String name, String description, Integer idMain, TaskStatus taskStatus) {
        super(name, description, taskStatus);
        this.idMain = idMain;
    }

    public SubTask(String name, String description, Integer idMain, TaskStatus taskStatus, Integer id) {
        super(id, name, description, taskStatus);
        this.idMain = idMain;
    }

    public int getIdMain() {
        return idMain;
    }
}
