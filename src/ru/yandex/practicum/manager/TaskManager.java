package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;

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
    ArrayList<Task> getTasks();

    // получить эпики
    ArrayList<Epic> getEpics();

    // получить подзадачи
    ArrayList<Subtask> getSubtasks();

    // получить подзадачи эпика
    ArrayList<Subtask> getEpicSubtasks(Epic epic);

    //получить список подзадач эпика по id
    ArrayList<Subtask> getSubtasks(Integer epicId);

    //получить список подзадач эпика по Object
    ArrayList<Subtask> getSubtasks(Epic epic);

    //отображение последних просмотренных пользователем задач
    ArrayList<Task> getHistory();

    // обновляем таск
    Task updateTask(Task task);

    // обновляем эпик
    Epic updateEpic(Epic epic);

    // обновить подзадачу
    Subtask updateSubtask(Subtask subtask);

    // обновить статус эпика
    void updateEpicStatus(Epic epic);

    // удалить таски
    void deleteTasks();

    // удалить эпики
    void deleteEpics();

    // удалить подзадачи
    void deleteSubtasks();

    // удалить таск по id
    Task deleteTask(Integer taskId);

    // удалить эпик по id
    Epic deleteEpic(Integer epicId);

    // удалить подзадачу по id
    Subtask deleteSubtask(Integer subTaskId);

    // удалить подзадачу по Object
    void deleteSubtask(Subtask subtask);

    //удалить все
    void deleteAll();

    // напечатать все таски/эпики/подзадачи
    void printAll();
}