import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    protected static TaskManager taskManager = new InMemoryTaskManager();
    protected static Task task;
    protected static Epic epic;
    protected static SubTask subtask;

    @BeforeEach
    void beforeEach() {
        task = new Task("addNewTaskDescription", "addNewTask");
        epic = new Epic("addNewEpicDescription", "addNewEpic");
        subtask = new SubTask("addNewSubtaskDescription", "addNewSubtask", 2, TaskStatus.NEW);
    }

    @Test
    void addNewTask() {
        int taskId = taskManager.createTask(task);
        Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertNotEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        int epicId = taskManager.createEpic(epic);
        Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают");

        final List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(3, epics.size(), "Неверное количество задач.");
        assertNotEquals(epic, epics.getFirst(), "Задачи не совпадают");
    }

    @Test
    void addNewSubtask() {
        taskManager.createTask(task);
        int epicId = taskManager.createEpic(epic);
        int subtaskId = taskManager.createSubTask(new SubTask("addNewSubtaskDescription", "addNewSubtask", epicId, TaskStatus.NEW));
        for(SubTask temp: taskManager.getSubTaskList()){
            System.out.println(temp.getId());
        }
        final SubTask savedSubtask = taskManager.getSubTaskList().getFirst();

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertNotEquals(subtask, savedSubtask, "Задачи не совпадают");

        final List<SubTask> subtasks = taskManager.getSubTaskList();

        assertNotNull(subtasks, "Задачи не возвращаются");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertNotEquals(subtask, subtasks.getFirst(), "Задачи не совпадают");
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
        assertNull(taskManager.getSubtask(subtask.getId()));
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

        assertNotEquals(taskManager.getHistory(), List.of(task, epic));
    }
}