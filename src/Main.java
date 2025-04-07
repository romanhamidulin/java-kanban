import java.util.Scanner;
import ru.yandex.practicum.taskmanager.TaskManager;
import ru.practicum.yandex.tasks.Task;
import ru.practicum.yandex.tasks.Epic;
import ru.practicum.yandex.tasks.Subtask;
import ru.practicum.yandex.tasks.TaskStatus;


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        Task compliteSprint = new Task("Начать проходить 4й спринт", "Управиться за 2 недели");
        Task compliteSprintAdded = taskManager.addTask(compliteSprint);
        System.out.println(compliteSprintAdded);

        Task compliteSprintUpdate = new Task(compliteSprint.getId(), "Пройти 4й спринт", "Можно и за 1 неделю",
                TaskStatus.IN_PROGRESS);
        Task compliteSprintUpdated = taskManager.updateTask(compliteSprintUpdate);
        System.out.println(compliteSprintUpdated);

        Epic completeJavaCore = new Epic("Пройти курс Java Core: введение", "Нужно успеть до 05.05.2025");
        taskManager.addEpic(completeJavaCore);
        System.out.println("Создали эпик без подзадач");
        System.out.println(completeJavaCore);
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
        System.out.println(completeJavaCore);
        System.out.println("Добавили в эпик подзадачи");
        //completeJavaCoreSubtask1.setStatus(TaskStatus.DONE);
        completeJavaCoreSubtask2.setStatus(TaskStatus.DONE);
        //completeJavaCoreSubtask3.setStatus(TaskStatus.DONE);
        //completeJavaCoreSubtask4.setStatus(TaskStatus.DONE);
        //completeJavaCoreSubtask5.setStatus(TaskStatus.DONE);
        System.out.println("Обновили в эпике подзадачи");
        //taskManager.updateSubtask(completeJavaCoreSubtask1);
        taskManager.updateSubtask(completeJavaCoreSubtask2);
        //taskManager.updateSubtask(completeJavaCoreSubtask3);
        //taskManager.updateSubtask(completeJavaCoreSubtask4);
        //taskManager.updateSubtask(completeJavaCoreSubtask5);
        System.out.println(completeJavaCore);
        System.out.println("Получаем подзадачи эпика");
        System.out.println(taskManager.getSubtasks(completeJavaCore));
        System.out.println("Удалили в эпике подзадачи");
        taskManager.deleteSubtask(completeJavaCoreSubtask2);
        System.out.println(completeJavaCore);

    }
}


