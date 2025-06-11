package ru.yandex.practicum.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.TaskStatus;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    protected Subtask subtask1;
    protected LocalDateTime startTime;
    protected Duration duration;


    @BeforeEach
    public void setTaskManager() throws IOException {
        taskManager = createTaskManager();
    }

    protected abstract T createTaskManager() throws IOException;

    @BeforeEach
    void setUp() throws IOException {
        startTime = LocalDateTime.now();
        duration = Duration.ofMinutes(20);
        task = new Task("Таск", "описание");
        task.setStartTime(startTime);
        task.setDuration(duration);
        epic = new Epic("Эпик", "Описание");
        epic.setStartTime(startTime);
        epic.setDuration(duration);
        subtask = new Subtask("Подзадача", "Описание1", 0);
        subtask.setStartTime(startTime.plus(Duration.ofMinutes(10)));
        subtask.setDuration(duration);
        subtask1 = new Subtask("Подзадача1", "Описание2", 0);
        subtask1.setStartTime(startTime.plus(Duration.ofMinutes(35)));
        subtask1.setDuration(duration);
    }

    @Test
    void addTask() {
        taskManager.addTask(task);
        assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    void addSubtask() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        assertEquals(2, taskManager.getSubtasks().size());
    }

    @Test
    void addEpic() {
        taskManager.addEpic(epic);
        Assertions.assertEquals(1, taskManager.getEpics().size());
    }

    @Test
    void getTasks() {
        taskManager.addTask(task);
        Assertions.assertEquals(1, taskManager.getTasks().size());
        Task task1 = new Task("Новая задача", "новое описание");
        task1.setDuration(duration);
        task1.setStartTime(startTime.plus(Duration.ofHours(2)));
        taskManager.addTask(task1);
        Assertions.assertEquals(2, taskManager.getTasks().size());
    }

    @Test
    void getSubtasks() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        Assertions.assertEquals(2, taskManager.getSubtasks().size());

        Subtask subTask1 = new Subtask("Новая задача", "новое описание", 0);
        subTask1.setDuration(duration);
        subTask1.setStartTime(startTime.plus(Duration.ofHours(4)));
        taskManager.addSubtask(subTask1);
        Assertions.assertEquals(3, taskManager.getSubtasks().size());
    }

    @Test
    void getEpics() {
        taskManager.addEpic(epic);
        Assertions.assertEquals(1, taskManager.getEpics().size());

        taskManager.addEpic(epic);
        Assertions.assertEquals(2, taskManager.getEpics().size());
    }

    @Test
    void getTaskById() {
        taskManager.addTask(task);
        Assertions.assertEquals(task, taskManager.getTaskById(0));
    }

    @Test
    void getSubtaskById() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        Assertions.assertEquals(subtask, taskManager.getSubtaskById(1));
    }

    @Test
    void getEpicById() {
        taskManager.addEpic(epic);
        Assertions.assertEquals(epic, taskManager.getEpicById(0));
    }

    @Test
    void deleteTasks() {
        taskManager.addTask(task);
        Task task1 = new Task("Новая задача", "новое описание");
        task1.setDuration(duration);
        task1.setStartTime(startTime.plus(Duration.ofHours(2)));
        taskManager.addTask(task1);
        Assertions.assertEquals(2, taskManager.getTasks().size());

        taskManager.deleteTasks();
        Assertions.assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void deleteSubtasks() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        Subtask subTask1 = new Subtask("Новая задача", "новое описание", 0);
        subTask1.setDuration(duration);
        subTask1.setStartTime(startTime.plus(Duration.ofHours(4)));
        taskManager.addSubtask(subTask1);
        Assertions.assertEquals(2, taskManager.getSubtasks().size());

        taskManager.deleteSubtasks();
        Assertions.assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void deleteEpics() {
        taskManager.addEpic(epic);
        taskManager.addEpic(epic);
        Assertions.assertEquals(2, taskManager.getEpics().size());

        taskManager.deleteEpics();
        Assertions.assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void deleteTaskByID() {
        taskManager.addTask(task);
        Assertions.assertEquals(1, taskManager.getTasks().size());

        taskManager.deleteTask(task.getId());
        Assertions.assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void deleteSubtaskById() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        int subTaskId = subtask.getId();
        Assertions.assertEquals(1, taskManager.getSubtasks().size());

        taskManager.deleteSubtask(subtask.getId());
        Assertions.assertEquals(0, taskManager.getSubtasks().size());

        ArrayList<Subtask> epicSubtasks = epic.getSubtaskList();
        Assertions.assertTrue(epicSubtasks.contains(subtask));
    }

    @Test
    void deleteEpicById() {
        taskManager.addEpic(epic);
        Assertions.assertEquals(1, taskManager.getEpics().size());

        taskManager.deleteEpic(epic.getId());
        Assertions.assertTrue(taskManager.getEpics().isEmpty());

    }

    @Test
    void updateTask() {
        taskManager.addTask(task);
        Assertions.assertEquals(1, taskManager.getTasks().size());

        task.setDescription("Новвое описание");
        taskManager.updateTask(task);
        Assertions.assertEquals(task, taskManager.getTaskById(0));

    }

    @Test
    void updateSubtask() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        Assertions.assertEquals(1, taskManager.getSubtasks().size());

        subtask.setDescription("Новвое описание");
        taskManager.updateSubtask(subtask);
        Assertions.assertEquals(subtask, taskManager.getSubtaskById(1));
    }

    @Test
    void updateEpic() {
        taskManager.addEpic(epic);
        Assertions.assertEquals(1, taskManager.getEpics().size());

        epic.setDescription("Новвое описание");
        taskManager.updateEpic(epic);
        Assertions.assertEquals(epic, taskManager.getEpicById(epic.getId()));
    }

    @Test
    void getEpicSubtasks() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        Assertions.assertTrue(epic.getSubtaskList().contains(subtask));
    }

    @Test
    public void newEpicStatusSubtasksIsEmpty(){
        final Epic epic1 = taskManager.addEpic(new Epic("Эпик 1",
                "Описание эпика 1"));
        taskManager.addEpic(epic1);
        assertEquals(TaskStatus.NEW, taskManager.getEpicById(epic1.getId()).getStatus());
    }
    @Test
    void epicStatusAllSubtasksIsNew() {
        final Epic epic1 = taskManager.addEpic(new Epic("Эпик 1",
                "Описание эпика 1"));
        final Subtask epicSubtask1 = taskManager.addSubtask(new Subtask("Подзадача 1",
                "Описание подзадачи 1", epic1.getId()));
        final Subtask epicSubtask2 = taskManager.addSubtask(new Subtask("Подзадача 2",
                "Описание подзадачи 2", epic1.getId()));
        taskManager.addEpic(epic1);
        taskManager.addSubtask(epicSubtask1);
        taskManager.addSubtask(epicSubtask2);
        assertEquals(TaskStatus.NEW, taskManager.getEpicById(epic1.getId()).getStatus());
    }

    @Test
    void epicStatusAllSubtasksIsDone() {
        final Epic epic1 = taskManager.addEpic(new Epic("Эпик 1",
                "Описание эпика 1"));
        final Subtask epicSubtask1 = taskManager.addSubtask(new Subtask(1,"Подзадача 1",
                "Описание подзадачи 1",TaskStatus.DONE, LocalDateTime.now(), Duration.ofMinutes(10),epic1.getId()));
        final Subtask epicSubtask2 = taskManager.addSubtask(new Subtask(2,"Подзадача 2",
                "Описание подзадачи 2",TaskStatus.DONE, LocalDateTime.now().plusMinutes(10),Duration.ofMinutes(20),epic1.getId()));
        assertEquals(TaskStatus.DONE, taskManager.getEpicById(epic1.getId()).getStatus());
    }

    @Test
    void epicStatusAllSubtasksInProgress() {
        final Epic epic1 = taskManager.addEpic(new Epic("Эпик 1",
                "Описание эпика 1"));
        final Subtask epicSubtask1 = taskManager.addSubtask(new Subtask(1,"Подзадача 1",
                "Описание подзадачи 1",TaskStatus.IN_PROGRESS, LocalDateTime.now(),Duration.ofMinutes(10),epic1.getId()));
        final Subtask epicSubtask2 = taskManager.addSubtask(new Subtask(2,"Подзадача 2",
                "Описание подзадачи 2",TaskStatus.IN_PROGRESS, LocalDateTime.now().plusMinutes(10),Duration.ofMinutes(20), epic1.getId()));
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(epic1.getId()).getStatus());
    }

    @Test
    void epicStatusAllSubtasksNewAndDone() {
        final Epic epic1 = taskManager.addEpic(new Epic("Эпик 1",
                "Описание эпика 1"));
        final Subtask epicSubtask1 = taskManager.addSubtask(new Subtask("Подзадача 1",
                "Описание подзадачи 1", epic1.getId()));
        final Subtask epicSubtask2 = taskManager.addSubtask(new Subtask(epicSubtask1.getId()+1,"Подзадача 2",
                "Описание подзадачи 2",TaskStatus.DONE, LocalDateTime.now(),Duration.ofMinutes(10), epic1.getId()));
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(epic1.getId()).getStatus());
    }

}