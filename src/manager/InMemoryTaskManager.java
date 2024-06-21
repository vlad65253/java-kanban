package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    private HashMap<Integer, Task> libraryTask = new HashMap<>();
    private HashMap<Integer, Epic> libraryEpic = new HashMap<>();
    private HashMap<Integer, SubTask> librarySubTask = new HashMap<>();
    private  HistoryManager historyManager;
    @Override
    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(libraryTask.values());
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(libraryEpic.values());
    }

    @Override
    public ArrayList<SubTask> getSubTaskList() {
        return new ArrayList<>(librarySubTask.values());
    }

    @Override
    public Task getTask(Integer id) {
        historyManager.add(libraryTask.get(id));
        return libraryTask.get(id);
    }

    @Override
    public SubTask getSubtask(Integer id) {
        historyManager.add(librarySubTask.get(id));
        return librarySubTask.get(id);
    }

    @Override
    public Epic getEpic(Integer id) {
        historyManager.add(libraryEpic.get(id));
        return libraryEpic.get(id);
    }

    @Override
    public void delAllTask() {
        libraryTask.clear();
    }

    @Override
    public void delAllEpic() {
        libraryEpic.clear();
        librarySubTask.clear();
    }

    @Override
    public void delAllSubTask() {
        for (Epic epic : libraryEpic.values()) {
            epic.getIdSubTask().clear();
        }
        librarySubTask.clear();
    }

    @Override
    public void delTaskById(Integer id) {
        libraryTask.remove(id);
    }

    @Override
    public void delEpicById(Integer id) {
        for (Integer subTaskId : libraryEpic.get(id).getIdSubTask()) {
            delSubTaskById(subTaskId);
        }
        libraryEpic.remove(id);
    }

    @Override
    public void delSubTaskById(Integer id) {
        Integer mainId = librarySubTask.get(id).getIdMain();
        libraryEpic.get(mainId).getIdSubTask().remove(id);
        librarySubTask.remove(id);
        checkStatus(mainId);
    }

    @Override
    public void createTask(Task task) {
        task.setId(id);
        libraryTask.put(id, task);
        id++;
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(id);
        libraryEpic.put(id, epic);
        id++;
    }

    @Override
    public void createSubTask(SubTask subTask) {
        subTask.setId(id);
        librarySubTask.put(subTask.getId(), subTask);
        libraryEpic.get(subTask.getIdMain()).getIdSubTask().add(subTask.getId());
        checkStatus(subTask.getIdMain());
        id++;
    }

    @Override
    public void updateTask(Task task) {
        if (libraryTask.containsKey(task.getId())) {
            libraryTask.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (libraryTask.containsKey(epic.getId())) {
            libraryTask.put(epic.getId(), epic);
        }
        checkStatus(epic.getId());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        for (SubTask subTaskEquals : librarySubTask.values()) {
            if (subTask.getId().equals(subTaskEquals.getId())) {
                if (subTaskEquals.equals(subTask)) {
                    break;
                } else {
                    librarySubTask.put(subTask.getId(), subTask);
                }
            }
        }
        checkStatus(subTask.getIdMain());
    }

    @Override
    public ArrayList<SubTask> getAllSubTaskForEpic(Integer id) {
        ArrayList<SubTask> subTaskForEpic = new ArrayList<>();
        for (Integer subTask : libraryEpic.get(id).getIdSubTask()) {
            subTaskForEpic.add(librarySubTask.get(subTask));
        }
        return subTaskForEpic;
    }

    @Override
    public void checkStatus(Integer id) {
        int countNew = 0;
        int countDone = 0;
        ArrayList<TaskStatus> subTaskForEpic = new ArrayList<>();
        for (Integer subTask : libraryEpic.get(id).getIdSubTask()) {
            subTaskForEpic.add(librarySubTask.get(subTask).getTaskStatus());
        }

        if (subTaskForEpic.isEmpty()) {
            libraryEpic.get(id).setTaskStatus(TaskStatus.NEW);
        }
        for (TaskStatus status : subTaskForEpic) {
            if (status == TaskStatus.IN_PROGRESS) {
                libraryEpic.get(id).setTaskStatus(TaskStatus.IN_PROGRESS);
                break;
            }
            if (status == TaskStatus.NEW) {
                countNew++;
            }
            if (status == TaskStatus.DONE) {
                countDone++;
            }
        }
        if (countDone == subTaskForEpic.size()) {
            libraryEpic.get(id).setTaskStatus(TaskStatus.DONE);
        } else if (countNew == subTaskForEpic.size()) {
            libraryEpic.get(id).setTaskStatus(TaskStatus.NEW);
        } else {
            libraryEpic.get(id).setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }
}
