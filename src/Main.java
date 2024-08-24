import manager.FileBackedTaskManager;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class Main {



    public static void main(String[] args) {
        File file = new File("File.csv");
        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);

        fileManager.createTask(new Task("Учёба", "С 18.00 до 22.00", TaskStatus.IN_PROGRESS));
        fileManager.createTask(new Task("Посмотреть фильм", "Отдохнуть", TaskStatus.NEW));
        fileManager.createTask(new Task("Погулять с собакой", "Выйти на улицу", TaskStatus.IN_PROGRESS));

        fileManager.createEpic(new Epic("Уборка", "Убраться в квартире"));
        fileManager.createEpic(new Epic("Чистка", "Убраться в машине"));

        fileManager.createSubTask(new SubTask("Помыть пол", "Уборка", 3, TaskStatus.NEW));
        fileManager.createSubTask(new SubTask("Пропылесосить ковры", "Уборка", 3, TaskStatus.IN_PROGRESS));

        fileManager.createSubTask(new SubTask("Пропылесосить салон", "Чистка", 4, TaskStatus.NEW));
        fileManager.createSubTask(new SubTask("Съездить на мойку", "Чистка", 4, TaskStatus.NEW));
        System.out.println("Вывод на экран задач");
        for (Task task : fileManager.getTaskList()) {
            System.out.println(task);
        }

        System.out.println("\nВывод на экран эпиков");
        for (Epic epic : fileManager.getEpicList()) {
            System.out.println(epic);
        }

        System.out.println("\nВывод на экран подзадачи");
        for (SubTask subtask : fileManager.getSubTaskList()) {
            System.out.println(subtask);
        }

        System.out.println("\nОбновление задачи");
        Task task1 = new Task("Вспомнил что собаки нет", "Купить продукты");
        task1.setId(2);
        task1.setTaskStatus(TaskStatus.IN_PROGRESS);
        fileManager.updateTask(task1);
        for (Task task : fileManager.getTaskList()) {
            System.out.println(task);
        }

        System.out.println("\nОбновление эпика'");
        Epic epicUpd = new Epic("Уборка new", "Убраться в квартире new");
        epicUpd.setId(3);
        fileManager.updateEpic(epicUpd);
        for (Epic epic1 : fileManager.getEpicList()) {
            System.out.println(epic1);
        }

        System.out.println("\nОбновление подзадачи");
        SubTask subtask = new SubTask("Чистка салона", "Пропылесосить салон", 4, TaskStatus.IN_PROGRESS);
        subtask.setTaskStatus(TaskStatus.IN_PROGRESS);
        subtask.setId(4);
        fileManager.updateSubTask(subtask);
        for (SubTask subtask1 : fileManager.getSubTaskList()) {
            System.out.println(subtask1);
        }

        System.out.println("\nПроверка обновился ли эпик");
        for (Epic epic : fileManager.getEpicList()) {
            System.out.println(epic);
        }

        System.out.println("\nПоиск по айди эпика");
        Epic epic = fileManager.getEpic(4);
        System.out.println(epic);

        System.out.println("\nПоиск по айди задачи");
        Task task = fileManager.getTask(1);
        System.out.println(task);

        System.out.println("\nПоиск по айди подзадачи");
        SubTask subtask1 = fileManager.getSubtask(6);
        System.out.println(subtask1);

        System.out.println("\nПоиск подзадач для эпика по айди");
        ArrayList<SubTask> subtaskByEpicId = fileManager.getAllSubTaskForEpic(3);
        for (SubTask subtask2 : subtaskByEpicId) {
            System.out.println(subtask2);
        }

        System.out.println("\nУдаление задачи по айди");
        fileManager.delTaskById(2);
        for (Task task2 : fileManager.getTaskList()) {
            System.out.println(task2);
        }

        System.out.println("\nУдаление подзадачи по айди");
        fileManager.delSubTaskById(6);
        for (SubTask subtask2 : fileManager.getSubTaskList()) {
            System.out.println(subtask2);
        }

        System.out.println("\nУдаление эпика по айди");
        fileManager.delEpicById(3);
        for (Epic epic1 : fileManager.getEpicList()) {
            System.out.println(epic1);
        }

        System.out.println("\nУдаление всех задач");
        fileManager.delAllTask();
        System.out.println(fileManager.getTaskList());

        System.out.println("\nУдаление всех подзадач");
        fileManager.delAllSubTask();
        System.out.println(fileManager.getSubTaskList());

        System.out.println("\nУдаление всех эпиков с их задачами");
        fileManager.delAllEpic();
        System.out.println(fileManager.getEpicList());
        System.out.println(fileManager.getSubTaskList());

        System.out.println("\nПолучаем историю просмотренных задач");
        for (Task history : fileManager.getHistory()) {
            System.out.println(history);
        }

    }
}
