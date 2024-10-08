package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int id = 1;
    protected final HashMap<Integer, Task> libraryTask = new HashMap<>();
    protected final HashMap<Integer, Epic> libraryEpic = new HashMap<>();
    protected final HashMap<Integer, SubTask> librarySubTask = new HashMap<>();
    private final HistoryManager historyManager = new InMemoryHistoryManager();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
        Task task = libraryTask.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubtask(Integer id) {
        SubTask subTask = librarySubTask.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Epic getEpic(Integer id) {
        Epic epic = libraryEpic.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void delAllTask() {
        for (Task taskForDelete : libraryTask.values()) {
            historyManager.remove(taskForDelete.getId());
        }
        libraryTask.clear();

    }

    @Override
    public void delAllEpic() {
        for (Task k : librarySubTask.values()) {
            historyManager.remove(k.getId());
        }
        for (Task k : libraryEpic.values()) {
            historyManager.remove(k.getId());
        }
        libraryEpic.clear();
        librarySubTask.clear();
    }

    @Override
    public void delAllSubTask() {
        for (Task k : librarySubTask.values()) {
            historyManager.remove(k.getId());
        }
        for (Epic epic : libraryEpic.values()) {
            epic.getIdSubTask().clear();
        }
        librarySubTask.clear();
    }

    @Override
    public void delTaskById(Integer id) {
        libraryTask.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void delEpicById(Integer id) {
        Epic epic = libraryEpic.get(id);
        if (epic != null) {
            for (Integer subTaskId : epic.getIdSubTask()) {
                librarySubTask.remove(subTaskId);
                historyManager.remove(subTaskId);
            }
            epic.getIdSubTask().clear();
        }
        libraryEpic.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void delSubTaskById(Integer id) {
        Integer mainId = librarySubTask.get(id).getIdMain();
        libraryEpic.get(mainId).delIdSubTask(id);
        librarySubTask.remove(id);
        checkStatus(mainId);
        historyManager.remove(id);
    }

    @Override
    public int createTask(Task task) {
        task.setId(id);
        libraryTask.put(id, task);
        id++;
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        epic.setId(id);
        libraryEpic.put(id, epic);
        id++;
        return epic.getId();
    }

    @Override
    public int createSubTask(SubTask subTask) {
        subTask.setId(id);
        if (libraryEpic.get(subTask.getIdMain()) != null) {
            librarySubTask.put(subTask.getId(), subTask);
            libraryEpic.get(subTask.getIdMain()).addIdSubTask(subTask.getId());
            checkStatus(subTask.getIdMain());
        }
        id++;
        return subTask.getId();
    }

    @Override
    public void updateTask(Task task) {
        if (libraryTask.containsKey(task.getId())) {
            libraryTask.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (libraryEpic.containsKey(epic.getId())) {
            libraryEpic.put(epic.getId(), epic);
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
