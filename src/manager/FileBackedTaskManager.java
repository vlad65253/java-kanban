package manager;

import exception.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static manager.TypeTask.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final String HEADER = "id,type,name,status,description,epic\n";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() {
        //сохранение текущее состояние менеджера в указанный файл
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл", e);
        }

        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            fw.write(HEADER);

            for (Task task : getTaskList()) {
                fw.write(ReplaceStrings.toString(task, TASK) + "\n");
            }

            for (Epic epic : getEpicList()) {
                fw.write(ReplaceStrings.toString(epic, EPIC) + "\n");
            }

            for (SubTask subTask : getSubTaskList()) {
                fw.write(ReplaceStrings.toString(subTask, SUBTASK) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось вписать значения в файл", e);
        }

    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);
        try (BufferedReader bf = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = bf.readLine();
            while (bf.ready()) {
                line = bf.readLine();

                Task task = ReplaceStrings.fromString(line);

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
    public void delAllTask() {
        super.delAllTask();
        save();
    }

    @Override
    public void delAllEpic() {
        super.delAllEpic();
        save();
    }

    @Override
    public void delAllSubTask() {
        super.delAllSubTask();
        save();
    }

    @Override
    public void delTaskById(Integer id) {
        super.delTaskById(id);
        save();
    }

    @Override
    public void delEpicById(Integer id) {
        super.delEpicById(id);
        save();
    }

    @Override
    public void delSubTaskById(Integer id) {
        super.delSubTaskById(id);
        save();
    }

    @Override
    public int createTask(Task task) {
        super.createTask(task);
        save();
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
        return subTask.getId();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

}
