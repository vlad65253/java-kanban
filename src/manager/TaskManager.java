package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int id;
    private HashMap<Integer, Task> libraryTask = new HashMap<>();
    private HashMap<Integer, Epic> libraryEpic = new HashMap<>();
    private HashMap<Integer, SubTask> librarySubTask = new HashMap<>();

    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(libraryTask.values());
    }

    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(libraryEpic.values());
    }

    public ArrayList<SubTask> getSubTaskList() {
        return new ArrayList<>(librarySubTask.values());
    }

    public void delAllTask() {
        libraryTask.clear();

    }

    public void delAllEpic() {
        libraryEpic.clear();
        librarySubTask.clear();
    }

    public void delAllSubTask() {
        for (Epic epic : libraryEpic.values()) {
            epic.getIdSubTask().clear();
        }
        librarySubTask.clear();
    }

    public void delTaskById(Integer id) {
        libraryTask.remove(id);

    }

    public void delEpicById(Integer id) {
        for (Integer subTaskId : libraryEpic.get(id).getIdSubTask()) {
            delSubTaskById(subTaskId);
        }
        libraryEpic.remove(id);

    }

    public void delSubTaskById(Integer id) {
        Integer mainId = librarySubTask.get(id).getIdMain();
        libraryEpic.get(mainId).getIdSubTask().remove(id);
        librarySubTask.remove(id);
        checkStatus(mainId);

    }

    public void createTask(Task task) {
        task.setId(id);
        libraryTask.put(id, task);
        id++;
    }

    public void createEpic(Epic epic) {
        epic.setId(id);
        libraryEpic.put(id, epic);
        id++;
    }

    public void createSubTask(SubTask subTask) {
        subTask.setId(id);
        librarySubTask.put(subTask.getId(), subTask);
        libraryEpic.get(subTask.getIdMain()).getIdSubTask().add(subTask.getId());
        checkStatus(subTask.getIdMain());
        id++;


    }

    public void updateTask(Task task) {
        if (libraryTask.containsKey(task.getId())) {
            libraryTask.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (libraryTask.containsKey(epic.getId())) {
            libraryTask.put(epic.getId(), epic);
        }
        checkStatus(epic.getId());
    }

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

    public ArrayList<SubTask> getAllSubTaskForEpic(Integer id) {
        ArrayList<SubTask> subTaskForEpic = new ArrayList<>();
        for (Integer subTask : libraryEpic.get(id).getIdSubTask()) {
            subTaskForEpic.add(librarySubTask.get(subTask));
        }
        return subTaskForEpic;
    }

    private void checkStatus(Integer id) {
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
}
