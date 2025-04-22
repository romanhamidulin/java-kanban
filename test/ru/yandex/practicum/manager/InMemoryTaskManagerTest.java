package ru.yandex.practicum.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.TaskStatus;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }
    @Test
    void addTask() {
        final Task task = taskManager.addTask(new Task("Таск 1", "Описание таск 1"));
        final Task savedTask = taskManager.getTaskById(task.getId());
        assertNotNull(savedTask, "Ошибка! Задача не найдена.");

        final ArrayList<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Ошибка! Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Ошибка! Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Ошибка! Задачи не совпадают.");


    }

    @Test
    void addEpicAndSubtask() {
        final Epic epic1 = taskManager.addEpic(new Epic("Эпик 1",
                "Описание эпика 1"));
        final Subtask epicSubtask1 = taskManager.addSubtask(new Subtask("Подзадача 1",
                "Описание подзадачи 1", epic1.getId()));
        final Subtask epicSubtask2 = taskManager.addSubtask(new Subtask("Подзадача 2",
                "Описание подзадачи 2", epic1.getId()));
        final Subtask epicSubtask3 = taskManager.addSubtask(new Subtask("Подзадача 3", "Описание подзадачи 3",
                epic1.getId()));
        final Epic savedEpic = taskManager.getEpicById(epic1.getId());
        final Subtask savedSubtask1 = taskManager.getSubtaskById(epicSubtask1.getId());
        final Subtask savedSubtask2 = taskManager.getSubtaskById(epicSubtask2.getId());
        final Subtask savedSubtask3 = taskManager.getSubtaskById(epicSubtask3.getId());
        assertNotNull(savedEpic, "Ошибка! Эпик не найден.");
        assertNotNull(savedSubtask2, "Ошибка! Подзадача не найдена.");
        assertEquals(epic1, savedEpic, "Ошибка! Эпики не совпадают.");
        assertEquals(epicSubtask1, savedSubtask1, "Ошибка! Подзадачи не совпадают.");
        assertEquals(epicSubtask3, savedSubtask3, "Ошибка! Подзадачи не совпадают.");

        final ArrayList<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Ошибка! Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Ошибка! Неверное количество эпиков.");
        assertEquals(epic1, epics.getFirst(), "Ошибка! Эпики не совпадают.");


        final ArrayList<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Ошибка! Подзадачи не возвращаются.");
        assertEquals(3, subtasks.size(), "Ошибка! Неверное количество подзадач.");
        assertEquals(savedSubtask1, subtasks.getFirst(), "Ошибка! Подзадачи не совпадают.");
    }

    @Test
    void updateTaskIsShouldReturnSameTaskId() {
        final Task task = taskManager.addTask(new Task("Таск 1", "Описание таск 1"));
        taskManager.addTask(task);
        final Task taskToUpdate = new Task(task.getId(), "новое имя", "новое описание", TaskStatus.DONE);
        final Task updatedTask = taskManager.updateTask(taskToUpdate);
        assertEquals(task.getId(), updatedTask.getId(), "Ошибка! Вернулась задачи с другим id");
    }
    @Test
    void updateEpicIsShouldReturnSameEpicId() {
        final Epic epic = taskManager.addEpic(new Epic("Эпик 1", "Описание эпика 1"));
        taskManager.addTask(epic);
        final Epic epicToUpdate = new Epic(epic.getId(), "новое имя", "новое описание", TaskStatus.DONE);
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
                TaskStatus.DONE, epic.getId());
        final Subtask updatedSubtask = taskManager.updateSubtask(subtaskToUpdate);
        assertEquals(subtask1, updatedSubtask, "Ошибка! Вернулась подзадача с другим id");
    }
    @Test
    public void deleteTasksIsShouldReturnEmptyList() {
        taskManager.addTask(new Task("Таск 1", "Описание таск 1"));
        taskManager.addTask(new Task("Таск 2", "Описание таск 2"));
        taskManager.deleteTasks();
        ArrayList<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty(), "Ошибка! После удаления задач список должен быть пуст.");
    }

    @Test
    public void deleteEpicsIsShouldReturnEmptyList() {
        taskManager.addEpic(new Epic("Эпик 1", "Описание эпика 1"));
        taskManager.deleteEpics();
        ArrayList<Epic> epics = taskManager.getEpics();
        assertTrue(epics.isEmpty(), "Ошибка! После удаления эпиков список должен быть пуст.");
    }

    @Test
    public void deleteSubtasksIsShouldReturnEmptyList() {
        final Epic epic1 = taskManager.addEpic(new Epic("Эпик 1",
                "Описание эпика 1"));
        final Subtask epicSubtask1 = taskManager.addSubtask(new Subtask("Подзадача 1",
                "Описание подзадачи 1", epic1.getId()));
        final Subtask epicSubtask2 = taskManager.addSubtask(new Subtask("Подзадача 2",
                "Описание подзадачи 2", epic1.getId()));
        final Subtask epicSubtask3 = taskManager.addSubtask(new Subtask("Подзадача 3", "Описание подзадачи 3",
                epic1.getId()));
        taskManager.deleteSubtasks();
        ArrayList<Subtask> subtasks = taskManager.getSubtasks();
        assertTrue(subtasks.isEmpty(), "Ошибка! После удаления подзадач список должен быть пуст.");
    }

    @Test
    public void deleteTaskByIdReturnNullIfKeyIsMissing() {
        taskManager.addTask(new Task(1, "Таск 1", "Описание таск 2", TaskStatus.DONE));
        taskManager.addTask(new Task(2, "Таск 2", "Описание таск 2", TaskStatus.IN_PROGRESS));
        assertNull(taskManager.deleteTask(3), "Ошибка! Попытка удалить несуществующий Task");
    }

    @Test
    public void deleteEpicByIdReturnNullIfKeyIsMissing() {
        taskManager.addEpic(new Epic(1, "Сделать ремонт", "Нужно успеть за отпуск", TaskStatus.IN_PROGRESS));
        assertNull(taskManager.deleteEpic(2), "Ошибка! Попытка удалить несуществующий Epic");
    }

    @Test
    public void deleteSubtaskByIdReturnNullIfKeyIsMissing() {
        final Epic epic1 = taskManager.addEpic(new Epic("Эпик 1",
                "Описание эпика 1"));
        final Subtask epicSubtask1 = taskManager.addSubtask(new Subtask("Подзадача 1",
                "Описание подзадачи 1", epic1.getId()));
        final Subtask epicSubtask2 = taskManager.addSubtask(new Subtask("Подзадача 2",
                "Описание подзадачи 2", epic1.getId()));
        final Subtask epicSubtask3 = taskManager.addSubtask(new Subtask("Подзадача 3", "Описание подзадачи 3",
                epic1.getId()));
        assertNull(taskManager.deleteSubtask(5));
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
                "Описание подзадачи 1",TaskStatus.DONE, epic1.getId()));
        final Subtask epicSubtask2 = taskManager.addSubtask(new Subtask(2,"Подзадача 2",
                "Описание подзадачи 2",TaskStatus.DONE, epic1.getId()));
        taskManager.addEpic(epic1);
        taskManager.addSubtask(epicSubtask1);
        taskManager.addSubtask(epicSubtask2);
        assertEquals(TaskStatus.DONE, taskManager.getEpicById(epic1.getId()).getStatus());
    }

    @Test
    void epicStatusAllSubtasksInProgress() {
        final Epic epic1 = taskManager.addEpic(new Epic("Эпик 1",
                "Описание эпика 1"));
        final Subtask epicSubtask1 = taskManager.addSubtask(new Subtask(1,"Подзадача 1",
                "Описание подзадачи 1",TaskStatus.IN_PROGRESS, epic1.getId()));
        final Subtask epicSubtask2 = taskManager.addSubtask(new Subtask(2,"Подзадача 2",
                "Описание подзадачи 2",TaskStatus.IN_PROGRESS, epic1.getId()));
        taskManager.addEpic(epic1);
        taskManager.addSubtask(epicSubtask1);
        taskManager.addSubtask(epicSubtask2);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(epic1.getId()).getStatus());
    }

    @Test
    void epicStatusAllSubtasksNewAndDone() {
        final Epic epic1 = taskManager.addEpic(new Epic("Эпик 1",
                "Описание эпика 1"));
        final Subtask epicSubtask1 = taskManager.addSubtask(new Subtask("Подзадача 1",
                "Описание подзадачи 1", epic1.getId()));
        final Subtask epicSubtask2 = taskManager.addSubtask(new Subtask(epicSubtask1.getId()+1,"Подзадача 2",
                "Описание подзадачи 2",TaskStatus.DONE, epic1.getId()));
        taskManager.addEpic(epic1);
        taskManager.addSubtask(epicSubtask1);
        taskManager.addSubtask(epicSubtask2);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(epic1.getId()).getStatus());
    }

    @Test
    void TaskCreatedEqualsTaskAdded() {
        Task task = new Task(1, "Таск 1", "Описание задачи 1", TaskStatus.DONE);
        taskManager.addTask(task);
        ArrayList<Task> list = taskManager.getTasks();
        Task taskAdded = list.getFirst();
        assertEquals(task.getId(), taskAdded.getId());
        assertEquals(task.getName(), taskAdded.getName());
        assertEquals(task.getDescription(), taskAdded.getDescription());
        assertEquals(task.getStatus(), taskAdded.getStatus());
    }
}