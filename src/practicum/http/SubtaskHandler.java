package practicum.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import practicum.managers.TaskManager;
import practicum.tasks.Endpoint;
import practicum.tasks.Epic;
import practicum.tasks.Subtask;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubtaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "subtasks");

        switch (endpoint) {
            case GET_ALL: {
                handleGetSubtasks(exchange);
                break;
            }
            case GET_ID: {
                handleGetSubtaskById(exchange);
                break;
            }
            case POST: {
                handleCreateSubtask(exchange);
                break;
            }
            case DELETE_ID: {
                handleDeleteSubtaskById(exchange);
                break;
            }
            default:
                writeResponse(exchange, "Такого эндпоинта не существует.", 404);
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        try {
            List<Subtask> allSubtasks = taskManager.getAllSubtasks();
            String response = allSubtasks.stream()
                    .map(Subtask::toString)
                    .collect(Collectors.joining("\n"));

            if (response.isEmpty()) {
                writeResponse(exchange, "Не найдены подзадачи.", 404);
            } else {
                writeResponse(exchange, response, 200);
            }
        } catch (Exception e) {
            writeResponse(exchange, "Не найдены подзадачи.", 404);
        }
    }

    private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> subtaskIdOpt = getObjectId(exchange);
        if (subtaskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор подзадачи.", 400);
            return;
        }

        int subtaskId = subtaskIdOpt.get();
        try {
            Subtask subtask = taskManager.getSubtaskById(subtaskId);
            String response = subtask.toString();
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            writeResponse(exchange, "Подзадачи с идентификатором " + subtaskId + " не существует.", 404);
        }
    }

    private void handleCreateSubtask(HttpExchange exchange) throws IOException {
        String requestBody = readText(exchange);
        try {
            Subtask subtask = gson.fromJson(requestBody, Subtask.class);
            Epic epicPrnt = taskManager.getEpicById(subtask.getEpicId());
            if (epicPrnt == null) {
                writeResponse(exchange, "Родительского эпика не найдено.", 404);
                return;
            }
            taskManager.createSubtask(subtask);
            String response = subtask.toString();
            writeResponse(exchange, response, 201);
            System.out.println("Подзадача с ID=" + subtask.getId() + "[" + subtask.getName() + "] успешно создана.");
        }  catch (JsonSyntaxException e) {
            System.err.println("Ошибка в формате JSON: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Некорректные входные данные: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    private void handleDeleteSubtaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> subtaskIdOpt = getObjectId(exchange);
        if (subtaskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор подзадачи.", 400);
            return;
        }

        int subtaskId = subtaskIdOpt.get();
        try {
            Subtask subtask = taskManager.getSubtaskById(subtaskId);
            String response = subtask.toString();
            taskManager.deleteSubtaskById(subtaskId);
            writeResponse(exchange, "Подзадача с идентификатором " + subtaskId + " удалена.", 200);
        } catch (Exception e) {
            writeResponse(exchange, "Подзадачи с идентификатором " + subtaskId + " не существует.", 404);
        }
    }
}
