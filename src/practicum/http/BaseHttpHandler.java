package practicum.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import practicum.tasks.Endpoint;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public abstract class BaseHttpHandler implements HttpHandler {

    protected void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(StandardCharsets.UTF_8));
        }
        exchange.close();
    }

    protected String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    private static boolean isInteger(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches("-?\\d+(\\.\\d+)?");
    }

    protected Endpoint getEndpoint(String requestPath, String requestMethod, String keyWord) {
        String[] pathParts = requestPath.split("/");

        if (pathParts[1].equals(keyWord)) {
            if (requestMethod.equals("GET") && pathParts.length == 4 && pathParts[3].equals("subtasks")) {
                return Endpoint.GET_ID_SUBTASKS;
            }
            if (requestMethod.equals("GET") && pathParts.length == 3) {
                return Endpoint.GET_ID;
            }
            if (requestMethod.equals("GET") && pathParts.length == 2) {
                return Endpoint.GET_ALL;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST;
            }
            if (requestMethod.equals("DELETE") && pathParts.length == 3) {
                return Endpoint.DELETE_ID;
            }
            if (requestMethod.equals("DELETE") && pathParts.length == 2) {
                return Endpoint.DELETE_ALL;
            }
        }
        return Endpoint.UNKNOWN;
    }

    protected Optional<Integer> getObjectId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
