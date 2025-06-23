package ru.yandex.practicum.http;

import com.google.gson.Gson;
import ru.yandex.practicum.http.server.HttpTaskServer;
import ru.yandex.practicum.manager.InMemoryTaskManager;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.enums.TaskStatus;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор httpServer.HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer((InMemoryTaskManager) manager);
    Gson gson = taskServer.getGson();

    public HttpTaskManagerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        manager.deleteTasks();
        manager.deleteEpics();
        manager.deleteSubtasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {

        Task task = new Task(0,"Таск", "Описание задачи",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));

        String taskJson = gson.toJson(task);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:9090/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());


        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Таск", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldGetTasksFromServer() throws IOException, InterruptedException {
        Task task = new Task(0,"Таск", "Описание задачи",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));

        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:9090/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response1.statusCode());
    }

    @Test
    public void shouldGetTaskFromServer() throws IOException, InterruptedException {
        Task task = new Task(0,"Таск", "Описание задачи",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));

        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:9090/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        URI url1 = URI.create("http://localhost:9090/tasks/0");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response1.statusCode());
    }

    @Test
    public void shouldDeleteTaskFromServer() throws IOException, InterruptedException {
        Task task = new Task(0,"Таск", "Описание задачи",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));

        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:9090/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        URI url1 = URI.create("http://localhost:9090/tasks/0");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response1.statusCode());
    }


    @Test
    public void shouldPostEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(0,"Эпик", "Описание эпика 1",  TaskStatus.NEW, LocalDateTime.of(2025, Month.JULY, 2, 2, 11), Duration.ofHours(12));
        Subtask subtask = new Subtask(1,
                "Подзадача",
                "Описание подзадачи",
                TaskStatus.NEW,
                LocalDateTime.of(2025, Month.JULY, 3, 2, 11),
                Duration.ofHours(2),0

        );
        epic.addSubtask(subtask);

        String taskJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:9090/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals(taskJson, gson.toJson(manager.getEpicById(0)));
    }

    @Test
    public void shouldGetEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(0,"Эпик", "Описание эпика 1",  TaskStatus.NEW, LocalDateTime.of(2025, Month.JULY, 2, 2, 11), Duration.ofHours(12));
        Subtask subtask = new Subtask(1,
                "Подзадача",
                "Описание подзадачи",
                TaskStatus.NEW,
                LocalDateTime.of(2025, Month.JULY, 3, 2, 11),
                Duration.ofHours(2),0

        );
        epic.addSubtask(subtask);

        String taskJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:9090/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        URI url1 = URI.create("http://localhost:9090/epics/0");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response1.statusCode());
    }

    @Test
    public void shouldDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(0,"Эпик", "Описание эпика 1",  TaskStatus.NEW, LocalDateTime.of(2025, Month.JULY, 2, 2, 11), Duration.ofHours(12));
        epic.updateEndTime();

        String taskJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:9090/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        URI url1 = URI.create("http://localhost:9090/epics/1");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertNull(manager.getEpicById(1));
        Assertions.assertEquals(200, response1.statusCode());
    }

    @Test
    public void shouldPostSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0,"Эпик", "Описание эпика 1",  TaskStatus.NEW, LocalDateTime.of(2025, Month.JULY, 2, 2, 11), Duration.ofHours(12));

        Subtask subtask = new Subtask(1,
                "Подзадача",
                "Описание подзадачи",
                  TaskStatus.NEW,
                LocalDateTime.of(2025, Month.JULY, 3, 2, 11),
                Duration.ofHours(2),0

        );
        epic.addSubtask(subtask);
        //epic.updateEndTime();

        String epicJson = gson.toJson(epic);
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:9090/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        URI url1 = URI.create("http://localhost:9090/subtasks");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());


        Assertions.assertEquals(201, response1.statusCode());
        Assertions.assertEquals(subtaskJson, gson.toJson(manager.getSubtaskById(1)));
    }

    @Test
    public void shouldGetSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0,"Эпик", "Описание эпика 1",  TaskStatus.NEW, LocalDateTime.of(2025, Month.JULY, 2, 2, 11), Duration.ofHours(12));

        Subtask subtask = new Subtask(1,
                "Подзадача",
                "Описание подзадачи",
                TaskStatus.NEW,
                LocalDateTime.of(2025, Month.JULY, 2, 2, 11),
                Duration.ofHours(2),0

        );
        epic.updateEndTime();


        String epicJson = gson.toJson(epic);
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:9090/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        URI url1 = URI.create("http://localhost:9090/subtasks");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:9090/subtasks/1");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response2.statusCode());
    }

    @Test
    public void shouldDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0,"Эпик", "Описание эпика 1",  TaskStatus.NEW, LocalDateTime.of(2025, Month.JULY, 2, 2, 11), Duration.ofHours(12));

        Subtask subtask = new Subtask(1,
                "Подзадача",
                "Описание подзадачи",
                TaskStatus.NEW,
                LocalDateTime.of(2025, Month.JULY, 2, 2, 11),
                Duration.ofHours(2),0

        );
        epic.updateEndTime();


        String epicJson = gson.toJson(epic);
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:9090/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        URI url1 = URI.create("http://localhost:9090/subtasks");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:9090/subtasks/1");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response2.statusCode());
        Assertions.assertNull(manager.getSubtaskById(1));
    }

    @Test
    public void shouldGetHistory() throws IOException, InterruptedException {
        Task task = new Task(1,"Задача 2", "Описание задачи 2",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));

        Task task1 = new Task(2,"Задача 3", "Описание задачи 3",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(5), Duration.ofMinutes(10));

        String taskJson = gson.toJson(task);
        String task1Json = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:9090/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(task1Json)).build();
        URI url1 = URI.create("http://localhost:9090/tasks/1");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url1).GET().build();
        URI url2 = URI.create("http://localhost:9090/tasks/2");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());

        URI url3 = URI.create("http://localhost:9090/history");
        HttpRequest request4 = HttpRequest.newBuilder().uri(url3).GET().build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response4.statusCode());
        Assertions.assertEquals(gson.toJson(manager.getHistory()), response4.body());
    }

    @Test
    public void shouldGetPrioritized() throws IOException, InterruptedException {
        Task task = new Task(1,"Задача 2", "Описание задачи 2",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));

        Task task1 = new Task(2,"Задача 3", "Описание задачи 3",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(5), Duration.ofMinutes(10));

        String taskJson = gson.toJson(task);
        String task1Json = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:9090/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(task1Json)).build();
        URI url1 = URI.create("http://localhost:9090/tasks/1");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url1).GET().build();
        URI url2 = URI.create("http://localhost:9090/tasks/2");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());

        URI url3 = URI.create("http://localhost:9090/prioritized");
        HttpRequest request4 = HttpRequest.newBuilder().uri(url3).GET().build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response4.statusCode());
        Assertions.assertEquals(gson.toJson(manager.getPrioritizedTasks()), response4.body());
    }
}
