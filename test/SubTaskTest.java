import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.SubTask;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    TaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void taskEqualsById() {
        SubTask subtask = new SubTask("Помыть пол", "Уборка", 3);
        SubTask subtask1 = new SubTask("Пропылесосить ковры", "Уборка", 3);

        taskManager.createSubTask(subtask);
        taskManager.createSubTask(subtask1);
        subtask1.setId(0);
        assertNotEquals(subtask, subtask1, "Это не одна задача");
    }
}