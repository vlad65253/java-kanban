package manager;

import exception.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static manager.TypeTask.SUBTASK;
import static manager.TypeTask.TASK;

public class FileBackedTaskManager extends InMemoryTaskManager{
    private final File file;
    private static final String HEADER = "id,type,name,status,description,epic\n";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }
    public void save(){
        //сохранение текущее состояние менеджера в указанный файл
        try{
            if(Files.exists(file.toPath())){
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл", e);
        }

        try(FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)){
            fw.write(HEADER);

            for(Task task : getTaskList()){
                fw.write(toString(task) + "\n");
            }

            for(Epic epic : getEpicList()){
                fw.write(toString(epic) + "\n");
            }

            for(SubTask subTask : getSubTaskList()){
                fw.write(toString(subTask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось вписать значения в файл", e);
        }

    }


    private static String getEpicIdForSubtask(Task task) {
        if (task.getTypeTask().equals(TypeTask.SUBTASK)) {
            return Integer.toString(((SubTask) task).getIdMain());
        }
        return "";
    }
    protected static String toString(Task task) {

        return task.getId() + "," +
                task.getTypeTask() + "," +
                task.getName() + "," +
                task.getTaskStatus() + "," +
                task.getDescription() + "," +
                getEpicIdForSubtask(task);
    }

    public static Task fromString(String str){
        String[] split = str.split(",");
        int id = Integer.parseInt(split[0]);
        String typeTask = split[1];
        String name = split[2];
        TaskStatus status = TaskStatus.valueOf(split[3]);
        String description = split[4];
        return new Task(id, name, description, status);
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);
        try (BufferedReader bf = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = bf.readLine();
            while (bf.ready()) {
                line = bf.readLine();

                Task task = fromString(line);

                if (task.getTypeTask().equals(TypeTask.EPIC)) {
                    fileManager.libraryEpic.put(task.getId(), (Epic) task);
                } else if (task.getTypeTask().equals(TypeTask.SUBTASK)) {
                    fileManager.librarySubTask.put(task.getId(), (SubTask) task);
                    fileManager.libraryEpic.get(((SubTask) task).getIdMain()).addIdSubTask(task.getId());
                } else {
                    fileManager.libraryTask.put(task.getId(), task);
                }
                if (fileManager.getId() < task.getId()) {
                    fileManager.setId(task.getId());
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось получить данные из файла", e);
        }
        return fileManager;
    }

    @Override
    public void delAllTask(){
        super.delAllTask();
        save();
    }
    @Override
    public void delAllEpic(){
        super.delAllEpic();
        save();
    }

    @Override
    public void delAllSubTask(){
        super.delAllSubTask();
        save();
    }

    @Override
    public void delTaskById(Integer id){
        super.delTaskById(id);
        save();
    }

    @Override
    public void delEpicById(Integer id){
        super.delEpicById(id);
        save();
    }

    @Override
    public void delSubTaskById(Integer id){
        super.delSubTaskById(id);
        save();
    }

    @Override
    public int createTask(Task task){
        super.createTask(task);
        save();
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic){
        super.createEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int createSubTask(SubTask subTask){
        super.createSubTask(subTask);
        save();
        return subTask.getId();
    }

    @Override
    public void updateTask(Task task){
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic){
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask){
        super.updateSubTask(subTask);
        save();
    }

}
