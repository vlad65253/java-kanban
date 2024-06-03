import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    static int id = 0;

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
        for (Epic epic : libraryEpic.values()) {
            for (Integer idSubTask : epic.getIdSubTask()) {
                delSubTaskById(idSubTask);
            }
        }
        libraryEpic.clear();
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
        epic.id = id;
        libraryEpic.put(id, epic);
        checkStatus(epic.getId());
        id++;
    }

    public void createSubTask(SubTask subTask) {
        subTask.id = id;
        librarySubTask.put(subTask.getId(), subTask);
        libraryEpic.get(subTask.getIdMain()).getIdSubTask().add(subTask.getId());
        checkStatus(subTask.getIdMain());
        id++;


    }

    public void updateTask(Task task) {
        if (!task.id.equals(libraryTask.get(task.id).id) ||
                !task.getTaskStatus().equals(libraryTask.get(task.id).getTaskStatus()) ||
                !task.getDescription().equals(libraryTask.get(task.id).getDescription()) ||
                !task.getName().equals(libraryTask.get(task.id).getName())) {
            libraryTask.put(task.id, task);
        }
    }

    public void updateEpic(Epic epic) {
        if (!epic.id.equals(libraryTask.get(epic.id).id) ||
                !epic.getTaskStatus().equals(libraryTask.get(epic.id).getTaskStatus()) ||
                !epic.getDescription().equals(libraryTask.get(epic.id).getDescription()) ||
                !epic.getName().equals(libraryTask.get(epic.id).getName())) {
            libraryEpic.put(epic.getId(), epic);
        }
        checkStatus(epic.getId());
    }

    public void updateSubTask(SubTask subTask) {
        for (SubTask subTaskEquals : librarySubTask.values()) {
            if (subTask.getId().equals(subTaskEquals.getId())) {
                if (subTaskEquals.equals(subTask)) {
                    break;
                } else {
                    librarySubTask.put(subTask.id, subTask);
                }
            }
        }
        checkStatus(subTask.getIdMain());
    }

    public ArrayList<SubTask> getAllSubTaskForEpic(Integer id) {
        ArrayList<SubTask> subTaskForEpic = new ArrayList<>();
        for (SubTask subTask : librarySubTask.values()) {
            if (subTask.getIdMain() == id) {
                subTaskForEpic.add(subTask);
            }
        }
        return subTaskForEpic;
    }

    public void checkStatus(Integer id) {
        int countNew = 0;
        int countDone = 0;
        ArrayList<TaskStatus> subTaskForEpic = new ArrayList<>();
        for (SubTask subTask : librarySubTask.values()) {
            if (subTask.getIdMain() == id) {
                subTaskForEpic.add(subTask.getTaskStatus());
            }
        }

        if (subTaskForEpic.isEmpty()) {
            libraryEpic.get(id).taskStatus = TaskStatus.NEW;
        }
        for (TaskStatus status : subTaskForEpic) {
            if (status == TaskStatus.IN_PROGRESS) {
                libraryEpic.get(id).taskStatus = TaskStatus.IN_PROGRESS;
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
            libraryEpic.get(id).taskStatus = TaskStatus.DONE;
        }
        if (countNew == subTaskForEpic.size()) {
            libraryEpic.get(id).taskStatus = TaskStatus.NEW;
        }
        if (countDone >= 1 & countNew > 0) {
            libraryEpic.get(id).taskStatus = TaskStatus.IN_PROGRESS;
        }
    }
}
