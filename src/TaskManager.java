import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    static int id = 0;

    HashMap<Integer, Task> libraryTask = new HashMap<>();
    HashMap<Integer, Epic> libraryEpic = new HashMap<>();
    HashMap<Integer, SubTask> librarySubTask = new HashMap<>();

    public ArrayList<String> getTaskList() {
        ArrayList<String> taskList = new ArrayList<>();
        for (Task k : libraryTask.values()) {

            taskList.add(k.name + " " + k.taskStatus);
        }
        return taskList;
    }

    public ArrayList<String> getEpicList() {
        ArrayList<String> epicList = new ArrayList<>();
        for (Task k : libraryEpic.values()) {
            epicList.add(k.name + " " + k.taskStatus);
        }
        return epicList;

    }

    public ArrayList<String> getSubTaskList() {
        ArrayList<String> subTaskList = new ArrayList<>();
        for (Task k : librarySubTask.values()) {
            subTaskList.add(k.name + " " + k.taskStatus);
        }
        return subTaskList;

    }

    public void delAllTask() {
        libraryTask.clear();

    }

    public void delAllEpic() {
        libraryEpic.clear();
    }

    public void delAllSubTask() {
        librarySubTask.clear();
    }

    public void delTaskById(Integer id) {
        libraryTask.remove(id);

    }

    public void delEpicById(Integer id) {
        for (Integer a : libraryEpic.get(id).idSubTask){
            delSubTaskById(a);
        }
        libraryEpic.remove(id);

    }

    public void delSubTaskById(Integer id) {
        //надо если есть это значение в idSubTask
        Integer mainId = librarySubTask.get(id).main_id;
        librarySubTask.remove(id);
        checkStatus(getProgressSubTaskForEpic(mainId), mainId);

    }

    public void createTask(Task task) {
        libraryTask.put(id, task);
        id++;
    }

    public void createEpic(Epic epic) {
        epic.id = id;
        libraryEpic.put(id, epic);
        checkStatus(getProgressSubTaskForEpic(epic.id), epic.id);
        id++;
    }

    public void createSubTask(SubTask subTask) {
        subTask.id = id;
        librarySubTask.put(subTask.id, subTask);
        libraryEpic.get(subTask.main_id).idSubTask.add(subTask.id);
        checkStatus(getProgressSubTaskForEpic(subTask.main_id), subTask.main_id);
        id++;


    }

    public void updateTask(Task task) {
        libraryTask.put(task.id, task);
    }

    public void updateEpic(Epic epic) {
        libraryEpic.put(epic.id, epic);
        checkStatus(getProgressSubTaskForEpic(epic.id), epic.id);
    }

    public void updateSubTask(SubTask subTask) {
        librarySubTask.put(subTask.id, subTask);
        checkStatus(getProgressSubTaskForEpic(subTask.main_id), subTask.main_id);
    }

    public ArrayList<String> getAllSubTaskForEpic(Integer id) {
        ArrayList<String> subTaskForEpic = new ArrayList<>();
        for (SubTask s : librarySubTask.values()) {
            if (s.main_id == id) {
                subTaskForEpic.add(s.name);
            }
        }
        return subTaskForEpic;
    }

    public ArrayList<TaskStatus> getProgressSubTaskForEpic(Integer id) {
        ArrayList<TaskStatus> subTaskForEpic = new ArrayList<>();
        for (SubTask s : librarySubTask.values()) {
            if (s.main_id == id) {
                subTaskForEpic.add(s.taskStatus);
            }
        }
        return subTaskForEpic;
    }

    public void checkStatus(ArrayList<TaskStatus> statusEpic, Integer id){
        int countNew = 0;
        int countDone = 0;
        if(statusEpic.isEmpty()){
            libraryEpic.get(id).taskStatus = TaskStatus.NEW;
        }
        for (TaskStatus status: statusEpic) {
            if(status == TaskStatus.IN_PROGRESS){
                libraryEpic.get(id).taskStatus = TaskStatus.IN_PROGRESS;
                break;
            }
            if(status == TaskStatus.NEW){
                libraryEpic.get(id).taskStatus = TaskStatus.NEW;
                countNew++;
            }
            if(status == TaskStatus.DONE){
                countDone++;
            }
        }
        if (countDone == statusEpic.size()){
            libraryEpic.get(id).taskStatus = TaskStatus.DONE;
        }
        if (countNew == statusEpic.size()){
            libraryEpic.get(id).taskStatus = TaskStatus.NEW;
        }
    }


}
