package practicum.http;

import com.sun.net.httpserver.HttpExchange;
import com.google.gson.*;
import practicum.tasks.*;
import practicum.managers.*;

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
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

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
            case DELETE: {
                handleDeleteTask(exchange);
                break;
            }
            default:
                writeResponse(exchange, "Такого эндпоинта не существует.", 404);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        List<Task> allTasks = taskManager.getAllTasks();
        String response = allTasks.stream()
                .map(Task::toString)
                .collect(Collectors.joining("\n"));
        writeResponse(exchange, response, 200);
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(exchange);

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

    private Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException e) {
            return Optional.empty();
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
                writeResponse(exchange, response, 200);
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

    private void handleDeleteTask(HttpExchange exchange) throws IOException{
        writeResponse(exchange, "Эндпоинт пока не реализован", 503);
    }


}