import exception.ManagerSaveException;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subtask;

    @BeforeEach
    void beforeEach() {
        task = new Task("addNewTaskDescription", "addNewTask");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(15));

        epic = new Epic("addNewEpicDescription", "addNewEpic");

        subtask = new SubTask("addNewSubtaskDescription", "addNewSubtask", 2);
        subtask.setStartTime(LocalDateTime.now().plusHours(1));
        subtask.setDuration(Duration.ofMinutes(15));
    }

    @Test
    void addNewTask() {
        int taskId = taskManager.createTask(task);
        Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        int epicId = taskManager.createEpic(epic);
        Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают");

        final List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.getFirst(), "Задачи не совпадают");
    }

    @Test
    void addNewSubtask() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);

        final int subtaskId = taskManager.createSubTask(subtask);
        final SubTask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают");

        final List<SubTask> subtasks = taskManager.getSubTaskList();

        assertNotNull(subtasks, "Задачи не возвращаются");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.getFirst(), "Задачи не совпадают");
    }

    @Test
    void shouldReturnTaskAndFindById() {
        taskManager.createTask(task);
        assertNotNull(taskManager.getTask(task.getId()));
    }

    @Test
    void shouldReturnEpicAndFindById() {
        taskManager.createEpic(epic);
        assertNotNull(taskManager.getEpic(epic.getId()));
    }

    @Test
    void shouldReturnSubtaskAndFindById() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);
        assertNotNull(taskManager.getSubtask(subtask.getId()));
    }

    @Test
    void deleteTasksAndShouldReturnEmptyList() {
        taskManager.delAllTask();
        List<Task> tasks = taskManager.getTaskList();
        assertTrue(tasks.isEmpty(), "Список не пуст");
    }

    @Test
    void deleteEpicsAndShouldReturnEmptyList() {
        taskManager.delAllEpic();
        List<Epic> epics = taskManager.getEpicList();
        assertTrue(epics.isEmpty(), "Список не пуст");
    }

    @Test
    void deleteSubtasksAndShouldReturnEmptyList() {
        taskManager.delAllSubTask();
        List<SubTask> subtasks = taskManager.getSubTaskList();
        assertTrue(subtasks.isEmpty(), "Список не пуст");
    }

    @Test
    void removeTaskById() {
        taskManager.createTask(task);
        assertNotNull(taskManager.getTask(task.getId()));

        taskManager.delTaskById(task.getId());
        assertEquals(0, taskManager.getTaskList().size());
    }

    @Test
    void shouldReturnEmptyListSubtasksAfterDeleteEpic() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);

        taskManager.delEpicById(1);

        assertEquals(0, epic.getIdSubTask().size());
    }

    @Test
    void removeSubtaskById() {
        taskManager.createTask(task);
        assertEquals(taskManager.getTaskList(), List.of(task));

        taskManager.delTaskById(task.getId());
        assertNotEquals(taskManager.getTaskList(), task);
    }

    @Test
    void getHistory() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);

        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());

        assertEquals(taskManager.getHistory(), List.of(task, epic));
    }

    @Test
    void getPrioritized() {
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(15));
        taskManager.createTask(task);

        taskManager.createEpic(epic);

        subtask.setStartTime(LocalDateTime.now().minusMinutes(60));
        subtask.setDuration(Duration.ofMinutes(15));
        taskManager.createSubTask(subtask);

        assertEquals(taskManager.getPrioritized(), List.of(subtask, task));
    }

    @Test
    void checkOnCorrectIntersection() {
        epic.setStartTime(LocalDateTime.of(2024, 8, 10, 10, 0));
        epic.setDuration(Duration.ofMinutes(15));
        taskManager.createEpic(epic);

        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(60));
        taskManager.createTask(task);

        subtask.setStartTime(LocalDateTime.now().plusMinutes(20));
        subtask.setDuration(Duration.ofMinutes(60));

        assertThrows(ManagerSaveException.class, () -> taskManager.createSubTask(subtask));
    }
}