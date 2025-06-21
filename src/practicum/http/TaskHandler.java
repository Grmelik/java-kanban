package practicum.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import practicum.managers.TaskManager;
import practicum.tasks.Endpoint;
import practicum.tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "tasks");

        switch (endpoint) {
            case GET_ALL: {
                handleGetTasks(exchange);
                break;
            }
            case GET_ID: {
                handleGetTaskById(exchange);
                break;
            }
            case POST: {
                handleCreateTask(exchange);
                break;
            }
            case DELETE_ALL: {
                handleDeleteTasks(exchange);
                break;
            }
            case DELETE_ID: {
                handleDeleteTaskById(exchange);
                break;
            }
            default:
                writeResponse(exchange, "Такого эндпоинта не существует.", 404);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        try {
            List<Task> allTasks = taskManager.getAllTasks();
            String response = allTasks.stream()
                    .map(Task::toString)
                    .collect(Collectors.joining("\n"));

            if (response.isEmpty()) {
                writeResponse(exchange, "Не найдены задачи.", 404);
            } else {
                writeResponse(exchange, response, 200);
            }
        } catch (Exception e) {
            writeResponse(exchange, "Не найдены задачи.", 404);
        }
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = getObjectId(exchange);
        if (taskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор задачи.", 400);
            return;
        }

        int taskId = taskIdOpt.get();
        try {
            Task task = taskManager.getTaskById(taskId);
            String response = task.toString();
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            writeResponse(exchange, "Задачи с идентификатором " + taskId + " не существует.", 404);
        }
    }

    private void handleCreateTask(HttpExchange exchange) throws IOException {
        String requestBody = readText(exchange);
        try {
            Task task = gson.fromJson(requestBody, Task.class);
            taskManager.createTask(task);
            if (taskManager.getTaskById(task.getId()) == null) {
                writeResponse(exchange, "Задача с ID=" + task.getId() + " пересекается с существующими.", 406);
            } else {
                String response = task.toString();
                writeResponse(exchange, response, 201);
                System.out.println("Задача с ID=" + task.getId() + "[" + task.getName() + "] успешно создана.");
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Ошибка в формате JSON: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Некорректные входные данные: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    private void handleDeleteTasks(HttpExchange exchange) throws IOException {
        try {
            List<Task> allTasks = taskManager.getAllTasks();
            if (allTasks.isEmpty()) {
                writeResponse(exchange, "Не найдены задачи для удаления.", 404);
                return;
            } else {
                taskManager.deleteAllTasks();
            }
            try {
                allTasks = taskManager.getAllTasks();
                if (allTasks.isEmpty()) {
                    writeResponse(exchange, "Все задачи удалены.", 200);
                } else {
                    writeResponse(exchange, "Не все задачи удалены.", 400);
                }
            } catch (Exception e) {
                writeResponse(exchange, "Все задачи удалены.", 200);
            }
        } catch (Exception e) {
            writeResponse(exchange, "Не найдены задачи для удаления.", 404);
        }
    }

    private void handleDeleteTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = getObjectId(exchange);
        if (taskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор задачи.", 400);
            return;
        }

        int taskId = taskIdOpt.get();
        try {
            Task task = taskManager.getTaskById(taskId);
            String response = task.toString();
            taskManager.deleteTaskById(taskId);
            writeResponse(exchange, "Задача с идентификатором " + taskId + " удалена.", 200);
        } catch (Exception e) {
            writeResponse(exchange, "Задачи с идентификатором " + taskId + " не существует.", 404);
        }
    }
}