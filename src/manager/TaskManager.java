package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

public interface TaskManager {
    ArrayList<Task> getTaskList();

    ArrayList<Epic> getEpicList();

    ArrayList<Task> getHistory();

    ArrayList<SubTask> getSubTaskList();

    Task getTask(Integer id);

    SubTask getSubtask(Integer id);

    Epic getEpic(Integer id);

    void delAllTask();

    void delAllEpic();

    void delAllSubTask();

    void delTaskById(Integer id);

    void delEpicById(Integer id);

    void delSubTaskById(Integer id);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    ArrayList<SubTask> getAllSubTaskForEpic(Integer id);
}
