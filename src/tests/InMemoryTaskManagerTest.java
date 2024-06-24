package tests;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static TaskManager taskManager;
    private static Task task;
    private static Epic epic;
    private static SubTask subtask;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
        task = new Task("Test addNewTask description", "Test addNewTask");
        epic = new Epic("Test addNewEpic description", "Test addNewEpic");
        subtask = new SubTask("Test addNewSubtask description", "Test addNewSubtask", 0, TaskStatus.NEW);
    }

    @Test
    void addNewTask() {
        final int taskId = taskManager.createTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final ArrayList<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        final int epicId = taskManager.createEpic(epic);
        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают");

        final ArrayList<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.getFirst(), "Задачи не совпадают");
    }

    @Test
    void addNewSubtask() {
        taskManager.createEpic(epic);

        final int subtaskId = taskManager.createSubTask(subtask);
        final SubTask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают");

        final ArrayList<SubTask> subtasks = taskManager.getSubTaskList();

        assertNotNull(subtasks, "Задачи не возвращаются");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.getFirst(), "Задачи не совпадают");
    }

    @Test
    void shouldReturnTaskAndFindById() {
        final Task task = new Task("addNewTaskDescription", "addNewTask");
        taskManager.createTask(task);
        assertNotNull(taskManager.getTask(task.getId()));
    }

    @Test
    void shouldReturnEpicAndFindById() {
        final Epic epic = new Epic("addNewEpicDescription", "addNewEpic");
        taskManager.createEpic(epic);
        assertNotNull(taskManager.getEpic(epic.getId()));
    }

    @Test
    void shouldReturnSubtaskAndFindById() {
        taskManager.createEpic(epic);
        final SubTask subtask = new SubTask("addNewSubtaskDescription", "addNewSubtask", epic.getId(), TaskStatus.NEW);
        taskManager.createSubTask(subtask);
        assertNotNull(taskManager.getSubtask(subtask.getId()));
    }

    @Test
    void deleteTasksAndShouldReturnEmptyList() {
        taskManager.delAllTask();
        ArrayList<Task> tasks = taskManager.getTaskList();
        assertTrue(tasks.isEmpty(), "Список не пуст");
    }

    @Test
    void deleteEpicsAndShouldReturnEmptyList() {
        taskManager.delAllEpic();
        ArrayList<Epic> epics = taskManager.getEpicList();
        assertTrue(epics.isEmpty(), "Список не пуст");
    }

    @Test
    void deleteSubtasksAndShouldReturnEmptyList() {
        taskManager.delAllSubTask();
        ArrayList<SubTask> subtasks = taskManager.getSubTaskList();
        assertTrue(subtasks.isEmpty(), "Список не пуст");
    }
}