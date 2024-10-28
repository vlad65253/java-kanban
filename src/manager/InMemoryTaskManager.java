package manager;

import exception.ManagerSaveException;
import exception.NotFoundException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {

    protected int id;
    protected final Map<Integer, Task> libraryTask;
    protected final Map<Integer, Epic> libraryEpic;
    protected final Map<Integer, SubTask> librarySubTask;
    protected final Set<Task> prioritized;

    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        historyManager = new InMemoryHistoryManager();
        id = 0;
        libraryTask = new HashMap<>();
        libraryEpic = new HashMap<>();
        librarySubTask = new HashMap<>();
        prioritized = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    private int generateId() {
        return ++id;
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
    public int createTask(Task task) {
        int newTaskId = generateId();
        task.setId(newTaskId);
        validatePrioritized(task);
        addPrioritized(task);
        libraryTask.put(newTaskId, task);
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        int newEpicId = generateId();
        epic.setId(newEpicId);
        libraryEpic.put(newEpicId, epic);
        return epic.getId();
    }

    @Override
    public int createSubTask(SubTask subtask) {
        int newSubtaskId = generateId();
        subtask.setId(newSubtaskId);
        validatePrioritized(subtask);
        addPrioritized(subtask);
        Epic epic = libraryEpic.get(subtask.getEpicId());
        if (epic != null) {
            librarySubTask.put(newSubtaskId, subtask);
            epic.addIdSubTask(newSubtaskId);
            updateEpicStatus(epic);
            updateEpicTime(epic);
        }
        return subtask.getId();
    }

    @Override
    public void delAllTask() {
        for (Task task : libraryTask.values()) {
            historyManager.remove(task.getId());
            prioritized.remove(task);
        }
        libraryTask.clear();
    }

    @Override
    public void delAllEpic() {
        for (SubTask subtask : librarySubTask.values()) {
            historyManager.remove(subtask.getId());
            prioritized.remove(subtask);
        }

        for (Epic epic : libraryEpic.values()) {
            historyManager.remove(epic.getId());
        }
        librarySubTask.clear();
        libraryEpic.clear();
    }

    @Override
    public void delAllSubTask() {
        prioritized.removeAll(libraryEpic.values());
        librarySubTask.clear();
        for (Epic epic : libraryEpic.values()) {
            epic.getIdSubTask().clear();
            updateEpicStatus(epic);
            updateEpicTime(epic);
        }
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
            for (Integer subtaskId : epic.getIdSubTask()) {
                prioritized.remove(librarySubTask.get(subtaskId));
                historyManager.remove(subtaskId);
                librarySubTask.remove(subtaskId);
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
            Epic epic = libraryEpic.get(subtask.getEpicId());
            epic.getIdSubTask().remove(id);
            updateEpicStatus(epic);
            updateEpicTime(epic);
            prioritized.remove(subtask);
            librarySubTask.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public Task getTask(Integer id) {
        Task task = libraryTask.get(id);
        historyManager.add(task);
        if (task == null) {
            throw new NotFoundException("Task с этим айди нет: " + id);
        }
        return task;
    }

    @Override
    public Epic getEpic(Integer id) {
        Epic epic = libraryEpic.get(id);
        historyManager.add(epic);
        if (epic == null) {
            throw new NotFoundException("Epic с этим айди нет: " + id);
        }
        return epic;
    }

    @Override
    public SubTask getSubtask(Integer id) {
        SubTask subtask = librarySubTask.get(id);
        historyManager.add(subtask);
        if (subtask == null) {
            throw new NotFoundException("Subtask с этим айди нет: " + id);
        }
        return subtask;
    }

    @Override
    public ArrayList<SubTask> getAllSubTaskForEpic(Integer id) {
        ArrayList<SubTask> subtasksNew = new ArrayList<>();
        Epic epic = libraryEpic.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getIdSubTask()) {
                subtasksNew.add(librarySubTask.get(subtaskId));
            }
        }
        return subtasksNew;
    }

    @Override
    public void updateTask(Task task) {
        validatePrioritized(task);
        addPrioritized(task);
        if (libraryTask.containsKey(task.getId())) {
            libraryTask.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (libraryEpic.containsKey(epic.getId())) {
            libraryEpic.replace(epic.getId(), epic);
            updateEpicStatus(epic);
            updateEpicTime(epic);
        }
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        if (!libraryEpic.containsKey(epic.getId())) {
            return;
        }

        ArrayList<SubTask> epicSubtasks = new ArrayList<>();

        int countNew = 0;
        int countDone = 0;

        for (int i = 0; i < epic.getIdSubTask().size(); i++) {
            epicSubtasks.add(librarySubTask.get(epic.getIdSubTask().get(i)));
        }

        for (SubTask subtask : epicSubtasks) {
            if (subtask.getStatus() == TaskStatus.DONE) {
                countDone++;
            } else if (subtask.getStatus() == TaskStatus.NEW) {
                countNew++;
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }

            if (countNew == epicSubtasks.size()) {
                epic.setStatus(TaskStatus.NEW);
            } else if (countDone == epicSubtasks.size()) {
                epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    private void updateEpicTime(Epic epic) {
        List<Task> subtaskList = getPrioritized().stream()
                .filter(task -> task.getType().equals(TypeTask.SUBTASK))
                .filter(task -> ((SubTask) task).getEpicId() == epic.getId())
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
    public void updateSubTask(SubTask newSubtask) {
        validatePrioritized(newSubtask);
        addPrioritized(newSubtask);
        if (!librarySubTask.containsKey(newSubtask.getId())) {
            return;
        }
        Epic epic = libraryEpic.get(newSubtask.getEpicId());
        if (epic == null) {
            return;
        }
        librarySubTask.replace(newSubtask.getId(), newSubtask);
        updateEpicStatus(epic);
        updateEpicTime(epic);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritized() {
        return new ArrayList<>(prioritized);
    }

    protected void addPrioritized(Task task) {
        if (task.getType().equals(TypeTask.EPIC)) return;
        // Если задача окажется эпиком, метод добавления будет завершён
        List<Task> taskList = getPrioritized();
        if (task.getStartTime() != null && task.getEndTime() != null) {
            for (Task task1 : taskList) {
                if (task1.getId() == task.getId()) prioritized.remove(task1);
                // Будет удалять задачу из отсортированного списка если она совпадает
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
}