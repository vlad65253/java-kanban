import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    protected static TaskManager taskManager = new InMemoryTaskManager();
    protected static Task task = new Task("addNewTaskDescription", "addNewTask", TaskStatus.NEW);
    protected static Epic epic = new Epic("addNewEpicDescription", "addNewEpic");
    protected static SubTask subtask = new SubTask("addNewSubtaskDescription", "addNewSubtask", 2, TaskStatus.NEW);

    @Test
    void addNewTask() {
        final int taskId = taskManager.createTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final ArrayList<Task> tasks = taskManager.getTaskList();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
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
        assertEquals(4, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.getFirst(), "Задачи не совпадают");
    }

    @Test
    void addNewSubtask() {
        taskManager.createTask(task);
        int epicId = taskManager.createEpic(epic);
        final int subtaskId = taskManager.createSubTask(subtask);
        final SubTask savedSubtask = taskManager.getSubTaskList().getLast();
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtaskId, 7, "Задачи не совпадают");

        final ArrayList<SubTask> subtasks = taskManager.getSubTaskList();

        assertNotNull(subtasks, "Задачи не возвращаются");
        assertEquals(2, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtaskId, subtasks.getFirst().getId(), "Задачи не совпадают");
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

    @AfterAll
    static void deleteTasksAndShouldReturnEmptyList() {
        taskManager.delAllTask();
        ArrayList<Task> tasks = taskManager.getTaskList();
        assertTrue(tasks.isEmpty(), "Список не пуст");
    }

    @AfterAll
    static void deleteEpicsAndShouldReturnEmptyList() {
        taskManager.delAllEpic();
        ArrayList<Epic> epics = taskManager.getEpicList();
        assertTrue(epics.isEmpty(), "Список не пуст");
    }

    @AfterAll
    static void deleteSubtasksAndShouldReturnEmptyList() {
        taskManager.delAllSubTask();
        ArrayList<SubTask> subtasks = taskManager.getSubTaskList();
        assertTrue(subtasks.isEmpty(), "Список не пуст");
    }

    @AfterAll
    static void deleteTaskByIdTest() {
        int taskId = taskManager.createTask(task);
        taskManager.delTaskById(taskId);
        assertNull(taskManager.getTask(taskId), "Задача не удалилась");
    }

    @AfterAll
    static void deleteEpicByIdTest() {
        int epicId = taskManager.createEpic(epic);
        taskManager.delEpicById(epicId);
        assertNull(taskManager.getEpic(epicId), "Задача не удалилась");
    }

    @AfterAll
    static void deleteSubTaskByIdTest() {
        taskManager.createTask(task);
        int epicId = taskManager.createEpic(epic);
        int subtaskId = taskManager.createSubTask(new SubTask("addNewSubtaskDescription", "addNewSubtask", epicId, TaskStatus.NEW));
        taskManager.delSubTaskById(subtaskId);
        assertNull(taskManager.getSubtask(subtaskId), "Задача не удалилась");
    }

    @Test
    void updateTaskTest() {
        int taskId = taskManager.createTask(task);
        taskManager.updateTask(new Task(taskId, "123", "456", TaskStatus.DONE));
        assertNotEquals(taskManager.getTask(taskId).getTaskStatus(), TaskStatus.NEW, "Задача не обновилась");
    }

    @Test
    void updateSubTaskTest() {
        taskManager.createTask(task);
        int epicId = taskManager.createEpic(epic);
        int subTaskId = taskManager.createSubTask(subtask);
        taskManager.updateSubTask(new SubTask(subTaskId, "123", "456", epicId, TaskStatus.DONE));
        assertEquals(taskManager.getSubtask(subTaskId).getTaskStatus(), TaskStatus.DONE, "Подзадача не обновилась");

    }

    @Test
    void updateEpicTest() {
        taskManager.createTask(task);
        int epicId = taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);
        taskManager.updateEpic(new Epic("123", "456", epicId));
        assertEquals(taskManager.getEpic(epicId).getName(), "123", "Эпик не обновился");
    }

    @Test
    void getSubTaskEpicMethodTest() {
        taskManager.createTask(task);
        int epicId = taskManager.createEpic(epic);
        int subTaskId = taskManager.createSubTask(subtask);
        ArrayList<SubTask> subTaskTemp = taskManager.getAllSubTaskForEpic(epicId);
        assertEquals(subTaskTemp.get(0).getId(), subTaskId, "выведен неверный список сабтасок эпика");
    }


}