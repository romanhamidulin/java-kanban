package ru.yandex.practicum.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.TaskStatus;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    //private static TaskManager taskManager;
    protected InMemoryTaskManager createTaskManager() throws IOException {
        taskManager = new InMemoryTaskManager();
        return taskManager;
    }

    @BeforeEach
    void setUp() throws IOException {
        //taskManager = new InMemoryTaskManager();
        super.setUp();
    }
    @Test
    void addTask() {
        final Task task = taskManager.addTask(new Task("Таск 1", "Описание таск 1"));
        final Task savedTask = taskManager.getTaskById(task.getId());
        assertNotNull(savedTask, "Ошибка! Задача не найдена.");

        final List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Ошибка! Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Ошибка! Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Ошибка! Задачи не совпадают.");


    }

    @Test
    void addEpicAndSubtask() {
        final Epic epic1 = taskManager.addEpic(new Epic("Эпик 1",
                "Описание эпика 1"));
        final Subtask epicSubtask1 = taskManager.addSubtask(new Subtask(1,"Подзадача 1",
                "Описание подзадачи 1",TaskStatus.NEW,LocalDateTime.now(),Duration.ofMinutes(10), epic1.getId()));
        final Subtask epicSubtask2 = taskManager.addSubtask(new Subtask(2,"Подзадача 2",
                "Описание подзадачи 2",TaskStatus.NEW,LocalDateTime.now().plusMinutes(10),Duration.ofMinutes(3), epic1.getId()));
        final Subtask epicSubtask3 = taskManager.addSubtask(new Subtask(3,"Подзадача 3", "Описание подзадачи 3",
                TaskStatus.NEW,LocalDateTime.now().plusMinutes(20),Duration.ofMinutes(10), epic1.getId()));
        final Epic savedEpic = taskManager.getEpicById(epic1.getId());
        final Subtask savedSubtask1 = taskManager.getSubtaskById(epicSubtask1.getId());
        final Subtask savedSubtask2 = taskManager.getSubtaskById(epicSubtask2.getId());
        final Subtask savedSubtask3 = taskManager.getSubtaskById(epicSubtask3.getId());
        assertNotNull(savedEpic, "Ошибка! Эпик не найден.");
        assertNotNull(savedSubtask2, "Ошибка! Подзадача не найдена.");
        assertEquals(epic1, savedEpic, "Ошибка! Эпики не совпадают.");
        assertEquals(epicSubtask1, savedSubtask1, "Ошибка! Подзадачи не совпадают.");
        assertEquals(epicSubtask3, savedSubtask3, "Ошибка! Подзадачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Ошибка! Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Ошибка! Неверное количество эпиков.");
        assertEquals(epic1, epics.getFirst(), "Ошибка! Эпики не совпадают.");


        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Ошибка! Подзадачи не возвращаются.");
        assertEquals(3, subtasks.size(), "Ошибка! Неверное количество подзадач.");
        assertEquals(savedSubtask1, subtasks.getFirst(), "Ошибка! Подзадачи не совпадают.");
    }

    @Test
    void updateTaskIsShouldReturnSameTaskId() {
        final Task task = taskManager.addTask(new Task("Таск 1", "Описание таск 1"));
        taskManager.addTask(task);
        final Task taskToUpdate = new Task(task.getId(), "новое имя", "новое описание", TaskStatus.DONE,task.getStartTime(), task.getDuration());
        final Task updatedTask = taskManager.updateTask(taskToUpdate);
        assertEquals(task.getId(), updatedTask.getId(), "Ошибка! Вернулась задачи с другим id");
    }
    @Test
    void updateEpicIsShouldReturnSameEpicId() {
        final Epic epic = taskManager.addEpic(new Epic("Эпик 1", "Описание эпика 1"));
        taskManager.addTask(epic);
        final Epic epicToUpdate = new Epic(epic.getId(), "новое имя", "новое описание", TaskStatus.DONE, epic.getStartTime(), epic.getDuration());
        final Task updatedEpic = taskManager.updateTask(epicToUpdate);
        assertEquals(epic.getId(), updatedEpic.getId(), "Ошибка! Вернулcя эпик с другим id");
    }
    @Test
    public void updateSubtaskIsShouldReturnSameSubtaskId() {
        final Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addEpic(epic);
        final Subtask subtask1 = new Subtask("Подзадача 1", "описание подзадачи 1", epic.getId());
        taskManager.addSubtask(subtask1);
        final Subtask subtaskToUpdate = new Subtask(subtask1.getId(), "новое имя", "новое описание",
                TaskStatus.DONE, subtask1.getStartTime(), subtask1.getDuration(),epic.getId());
        final Subtask updatedSubtask = taskManager.updateSubtask(subtaskToUpdate);
        assertEquals(subtask1.getId(), updatedSubtask.getId(), "Ошибка! Вернулась подзадача с другим id");
    }
    @Test
    public void deleteTasksIsShouldReturnEmptyList() {
        taskManager.addTask(new Task("Таск 1", "Описание таск 1"));
        taskManager.addTask(new Task("Таск 2", "Описание таск 2"));
        taskManager.deleteTasks();
        List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty(), "Ошибка! После удаления задач список должен быть пуст.");
    }

    @Test
    public void deleteEpicsIsShouldReturnEmptyList() {
        taskManager.addEpic(new Epic("Эпик 1", "Описание эпика 1"));
        taskManager.deleteEpics();
        List<Epic> epics = taskManager.getEpics();
        assertTrue(epics.isEmpty(), "Ошибка! После удаления эпиков список должен быть пуст.");
    }

    @Test
    public void deleteSubtasksIsShouldReturnEmptyList() {
        final Epic epic1 = taskManager.addEpic(new Epic("Эпик 1",
                "Описание эпика 1"));
        final Subtask epicSubtask1 = taskManager.addSubtask(new Subtask(1,"Подзадача 1",
                "Описание подзадачи 1",TaskStatus.NEW,LocalDateTime.now(),Duration.ofMinutes(10), epic1.getId()));
        final Subtask epicSubtask2 = taskManager.addSubtask(new Subtask(2,"Подзадача 2",
                "Описание подзадачи 2",TaskStatus.NEW,LocalDateTime.now().plusMinutes(10),Duration.ofMinutes(3), epic1.getId()));
        final Subtask epicSubtask3 = taskManager.addSubtask(new Subtask(3,"Подзадача 3", "Описание подзадачи 3",
                TaskStatus.NEW,LocalDateTime.now().plusMinutes(20),Duration.ofMinutes(10), epic1.getId()));
        taskManager.deleteSubtasks();
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertTrue(subtasks.isEmpty(), "Ошибка! После удаления подзадач список должен быть пуст.");
    }

    @Test
    public void deleteTaskByIdReturnNullIfKeyIsMissing() {
        taskManager.deleteTask(999);
        assertEquals(0, taskManager.getTasks().size());
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void deleteEpicByIdReturnNullIfKeyIsMissing() {
        taskManager.deleteEpic(999);
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getSubtasks().size());
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void deleteSubtaskByIdReturnNullIfKeyIsMissing() {
        taskManager.deleteSubtask(999);
        assertEquals(0, taskManager.getSubtasks().size());
        assertEquals(0, taskManager.getHistory().size());
    }


    @Test
    void TaskCreatedEqualsTaskAdded() {
        Task task = new Task(1, "Таск 1", "Описание задачи 1", TaskStatus.DONE, LocalDateTime.now(),Duration.ofMinutes(10));
        taskManager.addTask(task);
        List<Task> list = taskManager.getTasks();
        Task taskAdded = list.getFirst();
        assertEquals(task.getId(), taskAdded.getId());
        assertEquals(task.getName(), taskAdded.getName());
        assertEquals(task.getDescription(), taskAdded.getDescription());
        assertEquals(task.getStatus(), taskAdded.getStatus());
    }

    @Test
    public void shouldReturnPrioritizedTasks() {
        Task taskNext, taskPrev;
        TreeSet<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        if (prioritizedTasks.size() > 0) {
            Iterator taskIterator = prioritizedTasks.iterator();
            taskPrev = prioritizedTasks.first();
            while (taskIterator.hasNext()) {
                taskNext = (Task) taskIterator.next();
                if (!taskNext.equals(taskPrev)) {
                    assertTrue(taskNext.getStartTime().isAfter(taskPrev.getStartTime()) || taskNext.getStartTime().isEqual(taskPrev.getStartTime()), "getPrioritizedTasks() failed");
                }
                taskPrev = taskNext;
            }
        }
    }
}