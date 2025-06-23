package ru.yandex.practicum.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.manager.InMemoryTaskManager;
import ru.yandex.practicum.tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler {


    public TaskHandler(InMemoryTaskManager inMemoryTaskManager, Gson gson) {
        super(inMemoryTaskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {
            case "POST":
                String path = httpExchange.getRequestURI().getPath();
                String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                if (requestBody.isEmpty()) {
                    sendBadRequest(httpExchange);
                }
                Task newTask = gson.fromJson(requestBody, Task.class);
                if (path.split("/").length == 3) {
                    inMemoryTaskManager.updateTask(newTask);
                    if (inMemoryTaskManager.getTaskById(newTask.getId()) == null) {
                        sendHasInteractions(httpExchange);
                        break;
                    }
                    sendText(httpExchange, inMemoryTaskManager.getTaskById(newTask.getId()), 201);
                    break;
                }

                int taskId = inMemoryTaskManager.addTask(newTask).getId();
                if (inMemoryTaskManager.getTaskById(taskId) == null) {
                    sendHasInteractions(httpExchange);
                    break;
                }
                sendText(httpExchange, inMemoryTaskManager.getTaskById(taskId), 201);
                break;
            case "GET":
                String path1 = httpExchange.getRequestURI().getPath();
                if (path1.split("/").length == 3) {
                    int id = Integer.parseInt(path1.split("/")[2]);
                    if (inMemoryTaskManager.getTaskById(id) == null) {
                        sendNotFound(httpExchange);
                        break;
                    }
                    Task task = inMemoryTaskManager.getTaskById(id);
                    sendText(httpExchange, task, 200);
                    break;
                }
                sendText(httpExchange, inMemoryTaskManager.getTasks(), 200);
                break;
            case "DELETE":
                String path2 = httpExchange.getRequestURI().getPath();
                int id1 = Integer.parseInt(path2.split("/")[2]);
                inMemoryTaskManager.deleteTask(id1);
                sendDeleted(httpExchange);
        }
    }
}
