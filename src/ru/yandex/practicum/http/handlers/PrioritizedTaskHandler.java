package ru.yandex.practicum.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.manager.InMemoryTaskManager;

import java.io.IOException;

public class PrioritizedTaskHandler extends BaseHttpHandler {
    public PrioritizedTaskHandler(InMemoryTaskManager inMemoryTaskManager, Gson gson) {
        super(inMemoryTaskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (!httpExchange.getRequestMethod().equals("GET")) {
            sendWrongMethod(httpExchange);
        }
        sendText(httpExchange, inMemoryTaskManager.getPrioritizedTasks(), 200);
    }
}