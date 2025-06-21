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

public class EpicHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "epics");

        switch (endpoint) {
            case GET_ALL: {
                handleGetEpics(exchange);
                break;
            }
            case GET_ID: {
                handleGetEpicById(exchange);
                break;
            }
            case GET_ID_SUBTASKS: {
                handleGetSubtasksOfEpic(exchange);
                break;
            }
            case POST: {
                handleCreateEpic(exchange);
                break;
            }
            case DELETE_ALL: {
                handleDeleteEpics(exchange);
                break;
            }
            case DELETE_ID: {
                handleDeleteEpicById(exchange);
                break;
            }
            default:
                writeResponse(exchange, "Такого эндпоинта не существует.", 404);
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        try {
            List<Epic> allEpics = taskManager.getAllEpics();
            String response = allEpics.stream()
                    .map(Epic::toString)
                    .collect(Collectors.joining("\n"));

            if (response.isEmpty()) {
                writeResponse(exchange, "Не найдены эпики.", 404);
            } else {
                writeResponse(exchange, response, 200);
            }
        } catch (Exception e) {
            writeResponse(exchange, "Не найдены эпики.", 404);
        }
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        Optional<Integer> epicIdOpt = getObjectId(exchange);

        if (epicIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор эпика.", 400);
            return;
        }

        int epicId = epicIdOpt.get();
        try {
            Epic epic = taskManager.getEpicById(epicId);
            String response = epic.toString();
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            writeResponse(exchange, "Эпика с идентификатором " + epicId + " не существует.", 404);
        }
    }

    private void handleGetSubtasksOfEpic(HttpExchange exchange) throws IOException {
        Optional<Integer> epicIdOpt = getObjectId(exchange);
        if (epicIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор эпика.", 400);
            return;
        }

        int epicId = epicIdOpt.get();
        try {
            Epic epic = taskManager.getEpicById(epicId);
            try {
                List<Subtask> allSubtasks = taskManager.getSubtasksOfEpic(epicId);
                String response = allSubtasks.stream()
                        .map(Subtask::toString)
                        .collect(Collectors.joining("\n"));

                if (response.isEmpty()) {
                    writeResponse(exchange, "Не найдены подзадачи эпика с ID=" + epicId + ".", 404);
                } else {
                    writeResponse(exchange, response, 200);
                }
            } catch (Exception e) {
                writeResponse(exchange, "Не найдены подзадачи эпика с ID=" + epicId + ".", 404);
            }
            String response = epic.toString();
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            writeResponse(exchange, "Эпика с идентификатором " + epicId + " не существует.", 404);
        }
    }

    private void handleCreateEpic(HttpExchange exchange) throws IOException {
        String requestBody = readText(exchange);
        try {
            Epic epic = gson.fromJson(requestBody, Epic.class);
            taskManager.createEpic(epic);

            if (taskManager.getEpicById(epic.getId()) == null) {
                writeResponse(exchange, "Эпик с ID=" + epic.getId() + " не создан.", 404);
            } else {
                String response = epic.toString();
                writeResponse(exchange, response, 201);
                System.out.println("Эпик с ID=" + epic.getId() + "[" + epic.getName() + "] успешно создан.");
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Ошибка в формате JSON: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Некорректные входные данные: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    private void handleDeleteEpics(HttpExchange exchange) throws IOException {
        try {
            List<Epic> allEpics = taskManager.getAllEpics();
            if (allEpics.isEmpty()) {
                writeResponse(exchange, "Не найдены эпики для удаления.", 404);
                return;
            } else {
                taskManager.deleteAllEpics();
            }
            try {
                allEpics = taskManager.getAllEpics();
                if (allEpics.isEmpty()) {
                    writeResponse(exchange, "Все эпики удалены.", 200);
                } else {
                    writeResponse(exchange, "Не все эпики удалены.", 400);
                }
            } catch (Exception e) {
                writeResponse(exchange, "Все эпики удалены.", 200);
            }
        } catch (Exception e) {
            writeResponse(exchange, "Не найдены эпики для удаления.", 404);
        }
    }

    private void handleDeleteEpicById(HttpExchange exchange) throws IOException {
        Optional<Integer> epicIdOpt = getObjectId(exchange);
        if (epicIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор задачи.", 400);
            return;
        }

        int epicId = epicIdOpt.get();
        try {
            Epic epic = taskManager.getEpicById(epicId);
            String response = epic.toString();
            taskManager.deleteEpicById(epicId);
            writeResponse(exchange, "Эпик с идентификатором " + epicId + " удален.", 200);
        } catch (Exception e) {
            writeResponse(exchange, "Эпика с идентификатором " + epicId + " не существует.", 404);
        }
    }
}