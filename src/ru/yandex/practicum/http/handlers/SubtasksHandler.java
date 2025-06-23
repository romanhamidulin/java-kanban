package ru.yandex.practicum.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.manager.InMemoryTaskManager;
import ru.yandex.practicum.tasks.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubtasksHandler extends BaseHttpHandler {
    public SubtasksHandler(InMemoryTaskManager inMemoryTaskManager, Gson gson) {
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
                Subtask newSubtask = gson.fromJson(requestBody, Subtask.class);
                if (path.split("/").length == 3) {
                    inMemoryTaskManager.updateSubtask(newSubtask);
                    if (inMemoryTaskManager.getTaskById(newSubtask.getId()) == null) {
                        sendHasInteractions(httpExchange);
                        break;
                    }
                    sendText(httpExchange, inMemoryTaskManager.getSubtaskById(newSubtask.getId()), 201);
                    break;
                }
                int subtaskId = inMemoryTaskManager.addSubtask(newSubtask).getId();
                if (inMemoryTaskManager.getSubtaskById(subtaskId) == null) {
                    sendHasInteractions(httpExchange);
                    break;
                }
                sendText(httpExchange, inMemoryTaskManager.getSubtaskById(subtaskId), 201);
                break;
            case "GET":
                String path1 = httpExchange.getRequestURI().getPath();
                if (path1.split("/").length == 3) {
                    int id = Integer.parseInt(path1.split("/")[2]);
                    if (inMemoryTaskManager.getSubtaskById(id) == null) {
                        sendNotFound(httpExchange);
                        break;
                    }
                    Subtask subtask = inMemoryTaskManager.getSubtaskById(id);
                    sendText(httpExchange, subtask, 200);
                    break;
                }
                sendText(httpExchange, inMemoryTaskManager.getSubtasks(), 200);
                break;
            case "DELETE":
                String path2 = httpExchange.getRequestURI().getPath();
                int id1 = Integer.parseInt(path2.split("/")[2]);
                inMemoryTaskManager.deleteSubtask(id1);
                sendDeleted(httpExchange);
        }
    }
}