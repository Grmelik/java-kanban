package practicum.http;

import com.sun.net.httpserver.HttpExchange;
import practicum.managers.TaskManager;
import practicum.tasks.Endpoint;
import practicum.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HistoryHandler extends BaseHttpHandler{
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "history");

        if (Objects.requireNonNull(endpoint) == Endpoint.GET_ALL) {
            handleGetHistory(exchange);
        } else {
            writeResponse(exchange, "Такого эндпоинта не существует.", 404);
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        try {
            List<Task> allHistory = taskManager.getHistory();
            String response = allHistory.stream()
                    .map(Task::toString)
                    .collect(Collectors.joining("\n"));

            if (response.isEmpty()) {
                writeResponse(exchange, "История задач пустая.", 404);
            } else {
                writeResponse(exchange, response, 200);
            }
        } catch (Exception e) {
            writeResponse(exchange, "История задач пустая.", 404);
        }
    }
}
