import java.util.ArrayList;
import java.util.HashMap;

public class Task {
    Integer id;
    String name;
    String description;
    TaskStatus taskStatus;

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;

    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(Integer id, String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.id = id;

    }
}
