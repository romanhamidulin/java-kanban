import java.util.ArrayList;
import java.util.Scanner;

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
        System.out.println(completeJavaCore);
        Subtask completeJavaCoreSubtask1 = new Subtask("Пройти спринт 1", "Нужно успеть до 10.03.2025(мягкий дедлайн)",
                completeJavaCore.getId());
        Subtask completeJavaCoreSubtask2 = new Subtask("Пройти спринт 2", "Нужно успеть до 24.03.2025(мягкий дедлайн)",
                completeJavaCore.getId());
        taskManager.addSubtask(completeJavaCoreSubtask1);
        taskManager.addSubtask(completeJavaCoreSubtask2);
        System.out.println(completeJavaCore);
        completeJavaCoreSubtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(completeJavaCoreSubtask2);
        System.out.println(completeJavaCore);
    }
}


