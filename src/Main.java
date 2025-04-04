import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        while (true) {
            printMenu();
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    taskManager.printAllTasks();
                    break;
                case "2":
                    taskManager.deleteAllTasks();
                    break;
                case "3":
                    taskManager.getTask(Integer.parseInt(scanner.nextLine()));
                    break;
                case "4":
                    System.out.println("Введите название задачи");
                    String name = scanner.nextLine();
                    System.out.println("Введите описание задачи");
                    String description = scanner.nextLine();
                    System.out.println("Введите статус задачи: \n" +
                            "1. NEW — задача только создана, но к её выполнению ещё не приступили.\n" +
                            "2. IN_PROGRESS — над задачей ведётся работа.\n" +
                            "3. DONE — задача выполнена.");
                    int status = scanner.nextInt();
                    TaskStatus newStatus = null;
                    switch (status) {
                        case 1:
                            newStatus = TaskStatus.NEW;
                            break;
                        case 2:
                            newStatus = TaskStatus.IN_PROGRESS;
                            break;
                        case 3:
                            newStatus = TaskStatus.DONE;
                            break;
                    }
                    Task task = new Task(name, description, newStatus);
                    taskManager.addTask(task.getId(), task);
                    break;
                case "5":
                    break;
                case "6":

                    break;

            }
        }
    }

    public static void printMenu() {
        System.out.println("1. Получение списка всех задач.");
        System.out.println("2. Удаление всех задач.");
        System.out.println("3. Получение по идентификатору.");
        System.out.println("4. Создание. Сам объект должен передаваться в качестве параметра.");
        System.out.println("5. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.");
        System.out.println("6. Удаление по идентификатору.");
    }
}
