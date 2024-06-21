import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        taskManager.createTask(new Task("Учёба", "С 18.00 до 22.00", TaskStatus.IN_PROGRESS));
        taskManager.createTask(new Task("Посмотреть фильм", "Отдохнуть", TaskStatus.NEW));
        taskManager.createTask(new Task("Погулять с собакой", "Выйти на улицу", TaskStatus.IN_PROGRESS));

        taskManager.createEpic(new Epic("Уборка", "Убраться в квартире"));
        taskManager.createEpic(new Epic("Чистка", "Убраться в машине"));

        taskManager.createSubTask(new SubTask("Помыть пол", "Уборка", 3, TaskStatus.NEW));
        taskManager.createSubTask(new SubTask("Пропылесосить ковры", "Уборка", 3, TaskStatus.IN_PROGRESS));

        taskManager.createSubTask(new SubTask("Пропылесосить салон", "Чистка", 4, TaskStatus.NEW));
        taskManager.createSubTask(new SubTask("Съездить на мойку", "Чистка", 4, TaskStatus.NEW));
        System.out.println("Вывод на экран задач");
        for (Task task : taskManager.getTaskList()) {
            System.out.println(task);
        }

        System.out.println("\nВывод на экран эпиков");
        for (Epic epic : taskManager.getEpicList()) {
            System.out.println(epic);
        }

        System.out.println("\nВывод на экран подзадачи");
        for (SubTask subtask : taskManager.getSubTaskList()) {
            System.out.println(subtask);
        }

        System.out.println("\nОбновление задачи");
        Task task1 = new Task("Вспомнил, что собаки нет", "Купить продукты");
        task1.setId(2);
        task1.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task1);
        for (Task task : taskManager.getTaskList()) {
            System.out.println(task);
        }

        System.out.println("\nОбновление подзадачи");
        SubTask subtask = new SubTask("Чистка салона", "Пропылесосить салон", 4, TaskStatus.IN_PROGRESS);
        subtask.setTaskStatus(TaskStatus.IN_PROGRESS);
        subtask.setId(8);
        taskManager.updateSubTask(subtask);
        for (SubTask subtask1 : taskManager.getSubTaskList()) {
            System.out.println(subtask1);
        }

        System.out.println("\nПроверка обновился ли эпик");
        for (Epic epic : taskManager.getEpicList()) {
            System.out.println(epic);
        }

        System.out.println("\nПоиск по айди задачи");
        Task task = taskManager.getTask(1);
        System.out.println(task);

        System.out.println("\nПоиск по айди эпика");
        Epic epic = taskManager.getEpic(4);
        System.out.println(epic);

        System.out.println("\nПоиск по айди подзадачи");
        SubTask subtask1 = taskManager.getSubtask(5);
        System.out.println(subtask1);

        System.out.println("\nПоиск подзадач для эпика по айди");
        ArrayList<SubTask> subtaskByEpicId = taskManager.getAllSubTaskForEpic(3);
        for (SubTask subtask2 : subtaskByEpicId) {
            System.out.println(subtask2);
        }

        System.out.println("\nУдаление задачи по айди");
        taskManager.delTaskById(2);
        for (Task task2 : taskManager.getTaskList()) {
            System.out.println(task2);
        }

        System.out.println("\nУдаление подзадачи по айди");
        taskManager.delSubTaskById(7);
        for (SubTask subtask2 : taskManager.getSubTaskList()) {
            System.out.println(subtask2);
        }

        System.out.println("\nУдаление эпика по айди");
        taskManager.delEpicById(5);
        for (Epic epic1 : taskManager.getEpicList()) {
            System.out.println(epic1);
        }

        System.out.println("\nУдаление всех задач");
        taskManager.delAllTask();
        System.out.println(taskManager.getTaskList());

        System.out.println("\nУдаление всех подзадач");
        taskManager.delAllSubTask();
        System.out.println(taskManager.getSubTaskList());

        System.out.println("\nУдаление всех эпиков с их задачами");
        taskManager.delAllEpic();
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubTaskList());

        System.out.println("\nПолучаем историю просмотренных задач");
        for (Task history : taskManager.getHistory()) {
            System.out.println(history);
        }


    }
}
