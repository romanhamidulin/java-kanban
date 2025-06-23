package ru.yandex.practicum.http.handlers;

import ru.yandex.practicum.http.adapters.DurationAdapter;
import ru.yandex.practicum.http.adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.manager.InMemoryTaskManager;
import ru.yandex.practicum.http.responses.DeleteResponse;
import ru.yandex.practicum.http.responses.ErrorResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler implements HttpHandler {
    protected final InMemoryTaskManager inMemoryTaskManager;
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public BaseHttpHandler(InMemoryTaskManager inMemoryTaskManager) {
        this.inMemoryTaskManager = inMemoryTaskManager;
    }

    protected void sendWrongMethod(HttpExchange h) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse("неправильный метод");
        String responseJson = gson.toJson(errorResponse);
        byte[] resp = responseJson.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(405, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendBadRequest(HttpExchange h) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse("тело запроса пустое");
        String responseJson = gson.toJson(errorResponse);
        byte[] resp = responseJson.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendText(HttpExchange h, Object body, int code) throws IOException {
        String response = gson.toJson(body);
        byte[] resp = response.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse("Объект не найден");
        String responseJson = gson.toJson(errorResponse);
        byte[] resp = responseJson.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse("Задачи пересекаются");
        String responseJson = gson.toJson(errorResponse);
        byte[] resp = responseJson.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendDeleted(HttpExchange h) throws IOException {
        DeleteResponse deleteResponse = new DeleteResponse("Задача успешно удалена");
        String responseJson = gson.toJson(deleteResponse);
        byte[] resp = responseJson.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
}
