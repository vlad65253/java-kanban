import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest extends InMemoryTaskManagerTest {

    File file;

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