package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTaskList();

    ArrayList<Epic> getEpicList();

    List<Task> getHistory();

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

    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateEpicStatus(Epic epic);

    void updateSubTask(SubTask subTask);

    ArrayList<SubTask> getAllSubTaskForEpic(Integer id);

    List<Task> getPrioritized();
}
