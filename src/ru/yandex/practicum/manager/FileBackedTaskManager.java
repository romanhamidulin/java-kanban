package ru.yandex.practicum.manager;

import ru.yandex.practicum.enums.TaskStatus;
import ru.yandex.practicum.exception.ManagerSaveException;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.io.*;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        save();
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    public void deleteTask(Integer taskId) {
        super.deleteTask(taskId);
        save();
    }

    // удалить эпик по id
    @Override
    public void deleteEpic(Integer epicId) {
        super.deleteEpic(epicId);
        save();
    }

    // удалить подзадачу по id
    @Override
    public void deleteSubtask(Integer subTaskId) {
        super.deleteSubtask(subTaskId);
        save();
    }

    @Override
    public void deleteSubtask(Subtask subtask) {
        super.deleteSubtask(subtask);
        save();
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

    private void save() {
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write("id,type,name,status,description,startTime,duration,epic\n");
            for (Task task : getTasks()) {
                writer.write(toString(task));
                writer.write("\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(toString(epic));
                writer.write("\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(toString(subtask));
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения задач в файл", e);
        }
    }

    private String toString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%d",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                task.getStartTime(),
                task.getDuration(),
                task instanceof Subtask ? ((Subtask) task).getEpicId() : 0
        );
    }

    // Метод загрузки из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader buf = new BufferedReader(new FileReader(file))) {
            //пропустим заголовок
            String line = buf.readLine();
            while (buf.ready()) {
                line = buf.readLine();
                if (line.contains("EPIC")) {
                    Task epic = fromString(line);
                    manager.addEpic((Epic) epic);
                } else if (line.contains("SUBTASK")) {
                    Task subtask = fromString(line);
                    manager.addSubtask((Subtask) subtask);
                    manager.prioritizedTasks.add(subtask);
                } else {
                    Task task = fromString(line);
                    manager.addTask(task);
                    manager.prioritizedTasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки задачи из файла", e);
        }
        return manager;
    }

    private static Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        TaskStatus status = TaskStatus.valueOf(parts[3]);
        String description = parts[4];
        LocalDateTime startTime;
        if (parts[5].equals("null")) {
            startTime = null;
        } else {
            startTime = LocalDateTime.parse(parts[5]);
        }
        int epicId = parts.length > 5 ? Integer.parseInt(parts[7]) : 0;

        switch (type) {
            case "TASK":
                return new Task(id, name, description, status, startTime, Integer.parseInt(parts[7]));
            case "EPIC":
                return new Epic(id, name, description, status, startTime, Integer.parseInt(parts[7]));
            case "SUBTASK":
                return new Subtask(id, name, description, status, startTime, Integer.parseInt(parts[7]), epicId);
            default:
                throw new IllegalArgumentException("Неизвестный типа задачи: " + type);
        }
    }
}
