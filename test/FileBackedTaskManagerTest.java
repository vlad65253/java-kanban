import manager.FileBackedTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    File file;

    @BeforeEach
    void createFileBackTaskManagerTest() throws IOException {
        file = File.createTempFile("test", ".csv");
        taskManager = new FileBackedTaskManager(file);
    }

    @Test
    void loadFromFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        task.setStartTime(LocalDateTime.of(2024, 8, 10, 15, 0));
        task.setDuration(Duration.ofMinutes(15));
        fileBackedTaskManager.createTask(task);

        epic.setStartTime(LocalDateTime.of(2024, 8, 10, 16, 0));
        epic.setDuration(Duration.ofMinutes(15));
        fileBackedTaskManager.createEpic(epic);

        subtask.setStartTime(LocalDateTime.of(2024, 8, 10, 17, 0));
        subtask.setDuration(Duration.ofMinutes(15));
        fileBackedTaskManager.createSubTask(subtask); // Инициализируем менеджер и добавляем задачи в тестовый файл

        assertEquals(1, fileBackedTaskManager.getTaskList().size()); // проверяем, добавились ли задачи в списки
        assertEquals(1, fileBackedTaskManager.getEpicList().size());
        assertEquals(1, fileBackedTaskManager.getSubTaskList().size());

        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);
        /* создаём уже выгруженный менеджер из метода loadFromFile не создавая задач
        Сразу же проверяем одинаковые ли задачи в 1 и во 2 менеджере */

        assertEquals(fileBackedTaskManager.getTaskList(), fileManager.getTaskList());
        assertEquals(fileBackedTaskManager.getEpicList(), fileManager.getEpicList());
        assertEquals(fileBackedTaskManager.getSubTaskList(), fileManager.getSubTaskList());

        assertEquals(fileBackedTaskManager.getPrioritized(), fileManager.getPrioritized());
    }
}