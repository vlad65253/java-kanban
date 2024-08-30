import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {
    protected static TaskManager taskManager = new InMemoryTaskManager();
    File file;
    protected static Task task;
    protected static Epic epic;
    protected static SubTask subtask;

    @BeforeEach
    void beforeEach() {
        task = new Task("addNewTaskDescription", "addNewTask");
        epic = new Epic("addNewEpicDescription", "addNewEpic");
        subtask = new SubTask("addNewSubtaskDescription", "addNewSubtask", 2, TaskStatus.NEW);
    }

    FileBackedTaskManagerTest() throws IOException {
        file = File.createTempFile("test", ".csv");
        taskManager = new FileBackedTaskManager(file);
    }

    @Test
    void loadFromFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        fileBackedTaskManager.createTask(task);

        fileBackedTaskManager.createEpic(epic);

        fileBackedTaskManager.createSubTask(subtask);


        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);


        assertEquals(fileBackedTaskManager.getTaskList(), fileManager.getTaskList());
        assertEquals(fileBackedTaskManager.getEpicList(), fileManager.getEpicList());
        assertEquals(fileBackedTaskManager.getSubTaskList(), fileManager.getSubTaskList());
        fileBackedTaskManager.setId(0);
    }
}