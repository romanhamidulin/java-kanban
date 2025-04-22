import java.util.Scanner;
import ru.yandex.practicum.manager.InMemoryTaskManager;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.enums.TaskStatus;


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Поехали!");
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task compliteSprint = new Task("Начать проходить 4й спринт", "Управиться за 2 недели");
        Task compliteSprintAdded = taskManager.addTask(compliteSprint);
        System.out.println(compliteSprintAdded);

        Task compliteSprintUpdate = new Task(compliteSprint.getId(), "Пройти 4й спринт", "Можно и за 1 неделю",
                TaskStatus.IN_PROGRESS);
        Task compliteSprintUpdated = taskManager.updateTask(compliteSprintUpdate);
        System.out.println(compliteSprintUpdated);

        Epic completeJavaCore = new Epic("Пройти курс Java Core: введение", "Нужно успеть до 05.05.2025");
        taskManager.addEpic(completeJavaCore);
        Subtask completeJavaCoreSubtask1 = new Subtask("Пройти спринт 1", "Нужно успеть до 10.03.2025(мягкий дедлайн)",
                completeJavaCore.getId());
        Subtask completeJavaCoreSubtask2 = new Subtask("Пройти спринт 2", "Нужно успеть до 24.03.2025(мягкий дедлайн)",
                completeJavaCore.getId());
        Subtask completeJavaCoreSubtask3 = new Subtask("Пройти спринт 3", "Нужно успеть до 07.04.2025(мягкий дедлайн)",
                completeJavaCore.getId());
        Subtask completeJavaCoreSubtask4 = new Subtask("Пройти спринт 4", "Нужно успеть до 21.04.2025(мягкий дедлайн)",
                completeJavaCore.getId());
        Subtask completeJavaCoreSubtask5 = new Subtask("Пройти спринт 5", "Нужно успеть до 05.05.2025(мягкий дедлайн)",
                completeJavaCore.getId());
        taskManager.addSubtask(completeJavaCoreSubtask1);
        taskManager.addSubtask(completeJavaCoreSubtask2);
        taskManager.addSubtask(completeJavaCoreSubtask3);
        taskManager.addSubtask(completeJavaCoreSubtask4);
        taskManager.addSubtask(completeJavaCoreSubtask5);
        taskManager.getEpics().forEach(System.out::println);
        //completeJavaCoreSubtask1.setStatus(TaskStatus.DONE);
        Subtask newCompleteJavaCoreSubtask2 = new Subtask(completeJavaCoreSubtask2.getId(),completeJavaCoreSubtask2.getName(),
                completeJavaCoreSubtask2.getDescription(),TaskStatus.DONE,completeJavaCoreSubtask2.getEpicId());
        //completeJavaCoreSubtask3.setStatus(TaskStatus.DONE);
        //completeJavaCoreSubtask4.setStatus(TaskStatus.DONE);
        //completeJavaCoreSubtask5.setStatus(TaskStatus.DONE);
        //taskManager.updateSubtask(completeJavaCoreSubtask1);
        taskManager.updateSubtask(newCompleteJavaCoreSubtask2);
        taskManager.getEpics().forEach(System.out::println);
        //taskManager.updateSubtask(completeJavaCoreSubtask3);
        //taskManager.updateSubtask(completeJavaCoreSubtask4);
        //taskManager.updateSubtask(completeJavaCoreSubtask5)
        taskManager.deleteSubtask(newCompleteJavaCoreSubtask2);
        Epic newEpic = new Epic("Отремонтировать кровать", "Как можно быстрее");
        taskManager.addEpic(newEpic);
        Subtask newEpicSubtask1 = new Subtask("Найти что сломалось", "Провести внешний осмотр",
                newEpic.getId());
        taskManager.addSubtask(newEpicSubtask1);
        System.out.println(newEpic);
        Subtask updNewEpicSubtask1 = new Subtask(newEpicSubtask1.getId(),newEpicSubtask1.getName(),
                newEpicSubtask1.getDescription(),TaskStatus.DONE,newEpicSubtask1.getEpicId());
        taskManager.updateSubtask(updNewEpicSubtask1);
        System.out.println("Печатаем все");
        taskManager.printAll();
        Subtask newEpicSubtask2 = new Subtask("Купить сломанную деталь", "Провести внешний осмотр",
                newEpic.getId());
        taskManager.addSubtask(newEpicSubtask2);
        //taskManager.deleteSubtask(updNewEpicSubtask1);
        //taskManager.deleteSubtask(newEpicSubtask2);
        System.out.println("Печатаем все 2й раз");
        taskManager.getTasks().forEach(System.out::println);
        taskManager.getEpics().forEach(System.out::println);
        taskManager.getSubtasks().forEach(System.out::println);
    }
}


