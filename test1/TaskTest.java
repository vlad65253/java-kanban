import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    TaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void taskEqualsById() {
        Task task = new Task("Помыть пол", "Уборка");
        Task task1 = new Task("Пропылесосить ковры", "Уборка");

        taskManager.createTask(task);
        taskManager.createTask(task1);
        task1.setId(0);
        assertEquals(task.getId(), task1.getId(), "Это не одна задача");
    }
}