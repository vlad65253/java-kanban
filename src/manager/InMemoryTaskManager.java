package manager;

import exception.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int id = 1;
    protected final HashMap<Integer, Task> libraryTask = new HashMap<>();
    protected final HashMap<Integer, Epic> libraryEpic = new HashMap<>();
    protected final HashMap<Integer, SubTask> librarySubTask = new HashMap<>();
    private final HistoryManager historyManager = new InMemoryHistoryManager();
    protected final Set<Task> prioritized;

    public InMemoryTaskManager() {
        prioritized = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

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
            prioritized.remove(taskForDelete);
        }
        libraryTask.clear();

    }

    @Override
    public void delAllEpic() {
        for (Task subTask : librarySubTask.values()) {
            historyManager.remove(subTask.getId());
            prioritized.remove(subTask);
        }
        for (Task epic : libraryEpic.values()) {
            historyManager.remove(epic.getId());
        }
        libraryEpic.clear();
        librarySubTask.clear();
    }

    @Override
    public void delAllSubTask() {
        prioritized.removeAll(libraryEpic.values());
        for (Task k : librarySubTask.values()) {
            historyManager.remove(k.getId());
        }
        for (Epic epic : libraryEpic.values()) {
            epic.getIdSubTask().clear();
            updateEpicTime(epic);
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
                prioritized.remove(librarySubTask.get(subTaskId));
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
        SubTask subtask = librarySubTask.get(id);
        if (subtask != null) {
            Epic epic = libraryEpic.get(subtask.getIdMain());
            epic.getIdSubTask().remove(id);
            checkStatus(subtask.getIdMain());
            updateEpicTime(epic);
            prioritized.remove(subtask);
            librarySubTask.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public int createTask(Task task) {
        task.setId(id);
        validatePrioritized(task);
        addPrioritized(task);
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
        validatePrioritized(subTask);
        addPrioritized(subTask);
        Epic epic = libraryEpic.get(subTask.getIdMain());
        if (libraryEpic.get(subTask.getIdMain()) != null) {
            librarySubTask.put(subTask.getId(), subTask);
            libraryEpic.get(subTask.getIdMain()).addIdSubTask(subTask.getId());
            checkStatus(subTask.getIdMain());
            updateEpicTime(epic);
        }
        id++;
        return subTask.getId();
    }

    @Override
    public void updateTask(Task task) {
        if (libraryTask.containsKey(task.getId())) {
            libraryTask.put(task.getId(), task);
            validatePrioritized(task);
            addPrioritized(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (libraryEpic.containsKey(epic.getId())) {
            libraryEpic.put(epic.getId(), epic);
            checkStatus(epic.getId());
            updateEpicTime(epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        Epic epic = libraryEpic.get(subTask.getIdMain());
        for (SubTask subTaskEquals : librarySubTask.values()) {
            if (subTask.getId().equals(subTaskEquals.getId())) {
                if (subTaskEquals.equals(subTask)) {
                    break;
                } else {
                    librarySubTask.put(subTask.getId(), subTask);
                    checkStatus(subTask.getIdMain());
                    updateEpicTime(epic);
                    validatePrioritized(subTask);
                    addPrioritized(subTask);
                }
            }
        }

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

    private void updateEpicTime(Epic epic) {
        List<Task> subtaskList = getPrioritized().stream()
                .filter(task -> task.getTypeTask().equals(TypeTask.SUBTASK))
                .filter(task -> ((SubTask) task).getIdMain() == epic.getId())
                .toList();

        if (subtaskList.isEmpty()) {
            return;
        }
        Duration duration = Duration.ofMinutes(0);
        for (Task subtask : subtaskList) {
            duration = duration.plus(subtask.getDuration());
        }

        LocalDateTime startTime = subtaskList.getFirst().getStartTime();
        LocalDateTime endTime = subtaskList.getLast().getEndTime();

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);
    }

    @Override
    public List<Task> getPrioritized() {
        return new ArrayList<>(prioritized);
    }

    protected void addPrioritized(Task task) {
        if (task.getTypeTask().equals(TypeTask.EPIC)) return;
        List<Task> taskList = getPrioritized();
        if (task.getStartTime() != null && task.getEndTime() != null) {
            for (Task task1 : taskList) {
                if (task1.getId() == task.getId()) prioritized.remove(task1);
                if (checkForIntersection(task, task1)) {
                    return;
                }
            }
            prioritized.add(task);
        }
    }

    private boolean checkForIntersection(Task task1, Task task2) {
        return !task1.getEndTime().isBefore(task2.getStartTime()) &&
                !task1.getStartTime().isAfter(task2.getEndTime());

    }

    private void validatePrioritized(Task task) {
        if (task == null || task.getStartTime() == null) return;
        List<Task> taskList = getPrioritized();

        for (Task mapTask : taskList) {
            if (mapTask == task) {
                continue;
            }
            boolean taskIntersection = checkForIntersection(task, mapTask);

            if (taskIntersection) {
                throw new ManagerSaveException("Задачи - " + task.getId() + " и - " + mapTask.getId() + " пересекаются");
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
