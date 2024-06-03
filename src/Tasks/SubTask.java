package Tasks;

public class SubTask extends Task {
    private int idMain;

    public SubTask(String name, String description, Integer idMain, TaskStatus taskStatus) {
        super(name, description, taskStatus);
        this.idMain = idMain;

    }

    public SubTask(String name, String description, Integer idMain, TaskStatus taskStatus, Integer id) {
        super(name, description, taskStatus);
        this.idMain = idMain;
        this.id = id;

    }

    public int getIdMain() {
        return idMain;
    }
}
