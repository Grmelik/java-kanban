package practicum.http;

import com.sun.net.httpserver.HttpExchange;
import practicum.managers.TaskManager;
import practicum.tasks.Endpoint;
import practicum.tasks.Task;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PrioritizedHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "prioritized");

        if (Objects.requireNonNull(endpoint) == Endpoint.GET_ALL) {
            handleGetPrioritized(exchange);
        } else {
            writeResponse(exchange, "Такого эндпоинта не существует.", 404);
        }
    }

    private void handleGetPrioritized(HttpExchange exchange) throws IOException {
        try {
            Set<Task> allPrioritized = taskManager.getPrioritizedTasks();
            String response = allPrioritized.stream()
                    .map(Task::toString)
                    .collect(Collectors.joining("\n"));

            if (response.isEmpty()) {
                writeResponse(exchange, "Список приоритетных задач пуст.", 404);
            } else {
                writeResponse(exchange, response, 200);
            }
        } catch (Exception e) {
            writeResponse(exchange, "Список приоритетных задач пуст.", 404);
        }
    }
}