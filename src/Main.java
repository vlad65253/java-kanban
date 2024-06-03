import Manager.TaskManager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {

        TaskManager tm = new TaskManager();
        tm.createTask(new Task("поситаться", "надо постираться в общаге", TaskStatus.NEW));
        tm.createTask(new Task("Сделать домашку", "по матиматеке(((", TaskStatus.IN_PROGRESS));
        tm.createEpic(new Epic("построить дом", "дом в саду новый хотелось бы"));
        tm.createSubTask(new SubTask("купить материалы", "В стройремо", 2, TaskStatus.NEW));
        tm.createSubTask(new SubTask("нарисовать эскиз", "взять блокнотик и придумать",
                2, TaskStatus.IN_PROGRESS));
        tm.createEpic(new Epic("поиграть в компик", "в кс го"));
        tm.createSubTask(new SubTask("включить компик", "нажать кнопку вкл на пк",
                5, TaskStatus.DONE));
        System.out.println("Список обычных задач");
        System.out.println(tm.getTaskList());
        System.out.println("Список эпиков");
        System.out.println(tm.getEpicList());
        System.out.println("Список подзадач");
        System.out.println(tm.getSubTaskList());

        tm.updateSubTask(new SubTask("купить материалы", "В стройремо", 2, TaskStatus.IN_PROGRESS, 3));
        tm.updateSubTask(new SubTask("нарисовать эскиз", "взять блокнотик и придумать", 2, TaskStatus.DONE, 4));
        tm.updateTask(new Task(0, "поситаться", "надо постираться в общаге", TaskStatus.IN_PROGRESS));


        System.out.println("------------------------После обновления-------------------------------");
        System.out.println("Список обычных задач");
        System.out.println(tm.getTaskList());
        System.out.println("Список эпиков");
        System.out.println(tm.getEpicList());
        System.out.println("Список подзадач");
        System.out.println(tm.getSubTaskList());

        tm.delSubTaskById(3);

        System.out.println("------------------------После удаления-------------------------------");
        System.out.println("Список обычных задач");
        System.out.println(tm.getTaskList());
        System.out.println("Список эпиков");
        System.out.println(tm.getEpicList());
        System.out.println("Список подзадач");
        System.out.println(tm.getSubTaskList());

    }
}
