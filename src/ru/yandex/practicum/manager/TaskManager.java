package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    int nextId();

    // добавить таск
    Task addTask(Task task);

    // добавить эпик
    Epic addEpic(Epic epic);

    // добавить подзадачу
    Subtask addSubtask(Subtask subtask);

    // получить таск
    Task getTaskById(Integer taskId);

    // получить эпик
    Epic getEpicById(Integer epicId);

    // получить подзадачу
    Subtask getSubtaskById(Integer subTaskId);

    // получить таски
    List<Task> getTasks();

    // получить эпики
    List<Epic> getEpics();

    // получить подзадачи
    List<Subtask> getSubtasks();

    // получить подзадачи эпика
    List<Subtask> getEpicSubtasks(Epic epic);

    //получить список подзадач эпика по id
    List<Subtask> getSubtasks(Integer epicId);

    //получить список подзадач эпика по Object
    List<Subtask> getSubtasks(Epic epic);

    //отображение последних просмотренных пользователем задач
    List<Task> getHistory();

    // обновляем таск
    Task updateTask(Task task);

    // обновляем эпик
    Epic updateEpic(Epic epic);

    // обновить подзадачу
    Subtask updateSubtask(Subtask subtask);

    // обновить статус эпика
    //void updateEpicStatus(Epic epic);

    // удалить таски
    void deleteTasks();

    // удалить эпики
    void deleteEpics();

    // удалить подзадачи
    void deleteSubtasks();

    // удалить таск по id
    void deleteTask(Integer taskId);

    // удалить эпик по id
    void deleteEpic(Integer epicId);

    // удалить подзадачу по id
    void deleteSubtask(Integer subTaskId);

    // удалить подзадачу по Object
    void deleteSubtask(Subtask subtask);

    //удалить все
    void deleteAll();

    // напечатать все таски/эпики/подзадачи
    void printAll();

    TreeSet<Task> getPrioritizedTasks();

    //void setEpicEndTime(Epic epic);
}