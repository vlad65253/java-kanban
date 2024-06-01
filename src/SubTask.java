public class SubTask extends Task{
    int main_id;

    public SubTask(String name, String description, Integer main_id, TaskStatus taskStatus) {
        super(name, description, taskStatus);
        this.main_id = main_id;

    }
    public SubTask(String name, String description, Integer main_id, TaskStatus taskStatus, Integer id) {
        super(name, description, taskStatus);
        this.main_id = main_id;
        this.id = id;

    }

}
