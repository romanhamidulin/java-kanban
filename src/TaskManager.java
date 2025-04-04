import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    ArrayList<HashMap<Integer, Task>> taskList = new ArrayList<>();
    public TaskManager() {

    }

    public void addTask(Integer num, Task task) {
        tasks.put(num, task);
        taskList.add(tasks);
    }

    public void printAllTasks() {
        if (tasks.size() > 0) {
            for (HashMap<Integer, Task> array : taskList) {
               for (Task task : array.values()) {
                   System.out.println(task);
               }
            }
        } else {
            System.out.println("Список задач пуст");
        }
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task getTask(Integer num) {
        if (num <= 0) {
            System.out.println("Идентификатор задачи не может быть меньше или равным 0");
        } else {
            if (tasks.containsKey(num)) {
                return tasks.get(num);
            } else {
                System.out.println("Задачи с идентификаторм " + num + " не существует");
            }
        }

        return tasks.get(num);
    }

    public Task deleteTask(Integer num) {
        return tasks.get(num);
    }
}

