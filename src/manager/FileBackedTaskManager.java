package manager;

import exception.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    private static final String HEADER = "id,type,name,status,description,start,end,duration,epic\n";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);
        try (BufferedReader bf = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = bf.readLine(); // используем чтение 1 строки, что бы её пропустить
            while (bf.ready()) {
                line = bf.readLine();

                Task task = ReplaceStrings.fromString(line);

                if (task.getType().equals(TypeTask.EPIC)) {
                    fileManager.libraryEpic.put(task.getId(), (Epic) task);
                } else if (task.getType().equals(TypeTask.SUBTASK)) {
                    fileManager.librarySubTask.put(task.getId(), (SubTask) task);
                    fileManager.libraryEpic.get(((SubTask) task).getEpicId()).addIdSubTask(task.getId());
                } else {
                    fileManager.libraryTask.put(task.getId(), task);
                }
                if (fileManager.id < task.getId()) {
                    fileManager.id = task.getId();
                }
                fileManager.addPrioritized(task);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось получить данные из файла");
        }
        return fileManager;
    }

    private void save() {
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл для сохранения");
        }

        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            fw.write(HEADER); // Записываем хедер в первую строку

            for (Task task : getTaskList()) {
                fw.write(ReplaceStrings.toString(task) + "\n");
            }

            for (Epic epic : getEpicList()) {
                fw.write(ReplaceStrings.toString(epic) + "\n");
            }

            for (SubTask subtask : getSubTaskList()) {
                fw.write(ReplaceStrings.toString(subtask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось произвести сохранение");
        }
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
    public int createSubTask(SubTask subtask) {
        super.createSubTask(subtask);
        save();
        return subtask.getId();
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
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSubTask(SubTask newSubtask) {
        super.updateSubTask(newSubtask);
        save();
    }
}