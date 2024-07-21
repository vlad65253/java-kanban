
import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private final HistoryManager historyManager = new InMemoryHistoryManager();
    static Task task;
    static Epic epic;
    static SubTask subtask;

    @BeforeAll
    static void beforeAll() {
        task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        epic = new Epic("Test addNewTask", "Test addNewTask description");
        subtask = new SubTask("Test addNewTask", "Test addNewTask description", 2, TaskStatus.NEW);

        task.setId(1);
        epic.setId(2);
        subtask.setId(3);
    }

    @Test
    void add() {
        historyManager.add(task);

        assertNotNull(historyManager.getHistory(), "История пустая.");
        assertEquals(historyManager.getHistory().getFirst(), task, "Ожидаемое значение не совпадает");
    }

    @Test
    void add20TasksAndReturnUniqueTasksHistory() {
        for (int i = 0; i < 20; i++) {
            historyManager.add(task);
        }
        assertEquals(1, historyManager.getHistory().size(), "История не соответствует ожидаемому значению");
    }
    @Test
    void checkUniqueTask(){
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(task);

        assertNotEquals(task, historyManager.getHistory().getFirst(), "История не правильно записывает таски");
    }

    @Test
    void checkRemoveLastElement(){
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);

        historyManager.remove(task.getId());

        assertNotEquals(epic, historyManager.getHistory().getLast(), "Задача не удалилась из истории");
    }

    @Test
    void removeLastHistoryTask() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);

        historyManager.remove(subtask.getId());

        assertEquals(historyManager.getHistory(), List.of(task, epic));
    }
    @Test
    void removeMidHistoryTask() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);

        historyManager.remove(epic.getId());

        assertEquals(historyManager.getHistory(), List.of(task, subtask));
    }
}