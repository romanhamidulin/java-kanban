package ru.yandex.practicum.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.TaskStatus;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void getHistoryIsShouldBeListOf10() {
        for (int i = 0; i < 20; i++) {
            taskManager.addTask(new Task("Таск " + (i + 1), "Описание таска" + (i + 1)));
        }

        ArrayList<Task> tasks = taskManager.getTasks();
        for (Task task : tasks) {
            taskManager.getTaskById(task.getId());
        }

        ArrayList<Task> list = taskManager.getHistory();
        assertEquals(10, list.size(), "Ошибка! Количество элементов в истории больше максимально возможного ");
    }

    @Test
    void addHistoryShouldIgnoreNullTask() {
        historyManager.add(null);
        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(0, history.size());
    }

    @Test
    void getHistoryCorrectOrder() {
        Task task1 = new Task(1, "Task 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Описание 2", TaskStatus.IN_PROGRESS);
        Task task3 = new Task(3, "Task 3", "Описание 3", TaskStatus.DONE);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        for (Task task : taskManager.getTasks()) {
            taskManager.getTaskById(task.getId());
        }

        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
    }

    @Test
    public void getHistoryShouldReturnOldTaskAfterUpdate() {
        Task task = new Task("Таск 1", "описание таск 1");
        taskManager.addTask(task);
        taskManager.getTaskById(task.getId());
        taskManager.updateTask(new Task(task.getId(), "Обновленный таск 1",
                "описание обновленного таск 1", TaskStatus.IN_PROGRESS));
        ArrayList<Task> tasks = taskManager.getHistory();
        Task oldTask = tasks.getFirst();
        assertEquals(task.getName(), oldTask.getName(), "Ошибка! В истории не сохранилась старая версия задачи");
        assertEquals(task.getDescription(), oldTask.getDescription(),
                "Ошибка! В истории не сохранилась старая версия задачи");

    }

    @Test
    public void getHistoryShouldReturnOldEpicAfterUpdate() {
        Epic epic = new Epic("Эпик 1", "Описание эпик 1");
        taskManager.addEpic(epic);
        taskManager.getEpicById(epic.getId());
        taskManager.updateEpic(new Epic(epic.getId(), "Новое имя", "новое описание",
                TaskStatus.IN_PROGRESS));
        ArrayList<Task> epics = taskManager.getHistory();
        Epic oldEpic = (Epic) epics.getFirst();
        assertEquals(epic.getName(), oldEpic.getName(),
                "Ошибка! В истории не сохранилась старая версия эпика");
        assertEquals(epic.getDescription(), oldEpic.getDescription(),
                "Ошибка! В истории не сохранилась старая версия эпика");
    }

    @Test
    public void getHistoryShouldReturnOldSubtaskAfterUpdate() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпик 1");
        taskManager.addEpic(epic1);
        final Subtask epicSubtask1 = taskManager.addSubtask(new Subtask("Подзадача 1",
                "Описание подзадачи 1", epic1.getId()));
        taskManager.addSubtask(epicSubtask1);
        taskManager.getSubtaskById(epicSubtask1.getId());
        taskManager.updateSubtask(new Subtask(epicSubtask1.getId(), "Новое имя",
                "новое описание", TaskStatus.IN_PROGRESS, epic1.getId()));
        ArrayList<Task> subtasks = taskManager.getHistory();
        Subtask oldSubtask = (Subtask) subtasks.getFirst();
        assertEquals(epicSubtask1.getName(), oldSubtask.getName(),
                "Ошибка! В истории не сохранилась старая версия эпика");
        assertEquals(epicSubtask1.getDescription(), oldSubtask.getDescription(),
                "Ошибка! В истории не сохранилась старая версия эпика");
    }
}