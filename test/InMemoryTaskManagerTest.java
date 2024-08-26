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
        task = new Task( "Test addNewTask description", "Test addNewTask", TaskStatus.NEW);
        epic = new Epic("Test addNewEpic description", "Test addNewEpic");
        subtask = new SubTask("Test addNewSubtask description", "Test addNewSubtask", 1, TaskStatus.NEW);
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

    @Test
    void deleteTaskByIdTest(){
        int taskId = taskManager.createTask(task);
        taskManager.delTaskById(taskId);
        assertNull(taskManager.getTask(taskId), "Задача не удалилась");
    }

    @Test
    void deleteEpicByIdTest(){
        int epicId = taskManager.createEpic(epic);
        taskManager.delEpicById(epicId);
        assertNull(taskManager.getEpic(epicId), "Задача не удалилась");
    }

    @Test
    void deleteSubTaskByIdTest(){
        taskManager.createEpic(epic);
        int subtaskId = taskManager.createSubTask(subtask);
        taskManager.delSubTaskById(subtaskId);
        assertNull(taskManager.getSubtask(subtaskId), "Задача не удалилась");
    }

    @Test
    void updateTaskTest(){
        int taskId = taskManager.createTask(task);
        taskManager.updateTask(new Task(1, "123", "456", TaskStatus.DONE));
        assertEquals(taskManager.getTask(taskId).getTaskStatus(), TaskStatus.DONE, "Задача не обновилась");
    }

    @Test
    void updateSubTaskTest(){
        taskManager.createEpic(epic);
        int subTaskId = taskManager.createSubTask(subtask);
        taskManager.updateSubTask(new SubTask(2, "123", "456", 1, TaskStatus.DONE));
        assertEquals(taskManager.getSubtask(subTaskId).getTaskStatus(), TaskStatus.DONE, "Подзадача не обновилась");
    }

    @Test
    void updateEpicTest(){
        int epicId = taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);
        taskManager.updateEpic(new Epic("123", "456", 1));
        assertEquals(taskManager.getEpic(epicId).getName(), "123", "Эпик не обновился");
    }

    @Test
    void getSubTaskEpicMethodTest(){
        int epicId = taskManager.createEpic(epic);
        int subTaskId = taskManager.createSubTask(subtask);
        ArrayList<SubTask> subTaskTemp = taskManager.getAllSubTaskForEpic(epicId);
        assertEquals(subTaskTemp.get(0).getId(), subTaskId , "выведен неверный список сабтасок эпика");
    }


}