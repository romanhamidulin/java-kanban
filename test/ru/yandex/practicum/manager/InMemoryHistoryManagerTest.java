package ru.yandex.practicum.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.TaskStatus;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static TaskManager taskManager;
    private HistoryManager historyManager;
    private Task task;
    private Epic epic;
    private Subtask subTask;
    private LocalDateTime startTime;
    private Duration duration;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.now();
        duration = Duration.ofHours(1);
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
        task = new Task("Задача", "Описание");
        task.setId(0);
        task.setDuration(duration);
        task.setStartTime(startTime);
        epic = new Epic("Эпик", "описание");
        epic.setId(1);
        epic.setDuration(duration);
        epic.setStartTime(startTime);
        subTask = new Subtask("Подзадача", "Описание", 0);
        subTask.setId(2);
        subTask.setDuration(duration);
        subTask.setStartTime(startTime);
    }

    /*@Test
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
    }*/

    @Test
    void addHistoryShouldIgnoreNullTask() {
        historyManager.add(null);
        List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size());
    }

    @Test
    void getHistoryCorrectOrder() {
        Task task1 = new Task(1, "Task 1", "Описание 1", TaskStatus.NEW, LocalDateTime.now(),Duration.ofMinutes(10));
        Task task2 = new Task(2, "Task 2", "Описание 2", TaskStatus.IN_PROGRESS,LocalDateTime.now().plusMinutes(15),Duration.ofMinutes(2));
        Task task3 = new Task(3, "Task 3", "Описание 3", TaskStatus.DONE,LocalDateTime.now().plusMinutes(5),Duration.ofMinutes(10));
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
    }
    @Test
    void shouldRemoveTaskFromHistory() {
        historyManager.add(task);
        historyManager.remove(task.getId());
        List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size());
    }
    @Test
    void shouldNotFailForNotExistId() {
        historyManager.remove(100);
        List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size());
    }
    @Test
    void shouldReturnEmptyListIfHistoryIsEmpty() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }
    @Test
    public void getHistoryShouldReturnOldTaskAfterUpdate() {
        taskManager.addTask(task);
        taskManager.getTaskById(task.getId());
        taskManager.updateTask(new Task(task.getId(), "Обновленный таск 1",
                "описание обновленного таск 1", TaskStatus.IN_PROGRESS, LocalDateTime.now(),Duration.ofMinutes(10)));
        List<Task> tasks = taskManager.getHistory();
        Task oldTask = tasks.getFirst();
        assertEquals(task.getName(), oldTask.getName(), "Ошибка! В истории не сохранилась старая версия задачи");
        assertEquals(task.getDescription(), oldTask.getDescription(),
                "Ошибка! В истории не сохранилась старая версия задачи");

    }

    @Test
    public void getHistoryShouldReturnOldEpicAfterUpdate() {
        taskManager.addEpic(epic);
        taskManager.getEpicById(epic.getId());
        taskManager.updateEpic(new Epic(epic.getId(), "Новое имя", "новое описание",
                TaskStatus.IN_PROGRESS, LocalDateTime.now(),Duration.ofMinutes(10)));
        List<Task> epics = taskManager.getHistory();
        Epic oldEpic = (Epic) epics.getFirst();
        assertEquals(epic.getName(), oldEpic.getName(),
                "Ошибка! В истории не сохранилась старая версия эпика");
        assertEquals(epic.getDescription(), oldEpic.getDescription(),
                "Ошибка! В истории не сохранилась старая версия эпика");
    }

    @Test
    public void getHistoryShouldReturnOldSubtaskAfterUpdate() {
        taskManager.addEpic(epic);

        taskManager.addSubtask(subTask);
        taskManager.getSubtaskById(subTask.getId());
        taskManager.updateSubtask(new Subtask(subTask.getId(), "Новое имя",
                "новое описание", TaskStatus.IN_PROGRESS, LocalDateTime.now(), Duration.ofMinutes(10), epic.getId()));
        List<Task> subtasks = taskManager.getHistory();
        Subtask oldSubtask = (Subtask) subtasks.getFirst();
        assertEquals(subTask.getName(), oldSubtask.getName(),
                "Ошибка! В истории не сохранилась старая версия эпика");
        assertEquals(subTask.getDescription(), oldSubtask.getDescription(),
                "Ошибка! В истории не сохранилась старая версия эпика");
    }
    @Test
    void checkDuplicatesInHistory() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(task);
        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    void checkHistoryElementMovesToLastOneAfterOneMoreCall() {
        historyManager.add(task);
        historyManager.add(epic);
        assertEquals(task, historyManager.getHistory().getFirst());
        historyManager.add(task);
        assertEquals(task, historyManager.getHistory().getLast());
        assertNotEquals(task, historyManager.getHistory().getFirst());
    }

    @Test
    void removeTaskFromTheMiddleOfHistory() {
        historyManager.add(task);
        historyManager.add(subTask);
        historyManager.add(epic);
        assertEquals(3, historyManager.getHistory().size());
        historyManager.remove(epic.getId());
        assertEquals(List.of(task, subTask), historyManager.getHistory());
    }

    @Test
    void removeTaskFromTheEndOfHistory() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        assertEquals(3, historyManager.getHistory().size());
        historyManager.remove(subTask.getId());
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(epic, historyManager.getHistory().getLast());
        assertFalse(historyManager.getHistory().contains(subTask));
    }

    @Test
    void removeFirstTaskFromHistory() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        assertEquals(3, historyManager.getHistory().size());
        historyManager.remove(task.getId());
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(epic, historyManager.getHistory().getFirst());
    }
}