package ru.yandex.practicum.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.TaskStatus;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{
    //private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        taskManager = new FileBackedTaskManager(tempFile);
        super.setUp();
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void addAndSaveTask() {
        Task task = new Task(1,"Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        taskManager.addTask(task);

        List<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));

        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        tasks = taskManager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));
    }

    @Test
    void addAndSaveEpic() {
        Epic epic = new Epic(1,"Epic 1", "Description 1",  TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        taskManager.addEpic(epic);

        List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size());
        assertEquals(epic, epics.get(0));

        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        epics = taskManager.getEpics();
        assertEquals(1, epics.size());
        assertEquals(epic, epics.get(0));
    }

    @Test
    void addAndSaveSubtask() {
        Epic epic = new Epic(1,"Epic 1", "Description 1", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask(2,"Subtask 1", "Description 1", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(10), epic.getId());
        taskManager.addSubtask(subtask);

        List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(1, subtasks.size());
        assertEquals(subtask, subtasks.get(0));

        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        subtasks = taskManager.getSubtasks();
        assertEquals(1, subtasks.size());
        assertEquals(subtask, subtasks.get(0));
    }

    @Test
    void testSaveAndLoadMultipleTasks() {
        Task task1 = new Task(1,"Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        Task task2 = new Task(2,"Task 2", "Description 2", TaskStatus.DONE, LocalDateTime.now().plusMinutes(15), Duration.ofMinutes(10));
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        List<Task> tasks = taskManager.getTasks();
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));

        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        tasks = taskManager.getTasks();
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void testSaveAndLoadEmptyFile() {
        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testSaveAndLoadAfterDeletingTask() {
        Task task = new Task(1,"Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        taskManager.addTask(task);
        taskManager.deleteTask(task.getId());

        List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty());

        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testSaveAndLoadAfterUpdatingTask() {
        Task task = new Task(1,"Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        taskManager.addTask(task);
        task.setDescription("Updated Description");
        taskManager.updateTask(task);

        List<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals("Updated Description", tasks.get(0).getDescription());

        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        tasks = taskManager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals("Updated Description", tasks.get(0).getDescription());
    }
}