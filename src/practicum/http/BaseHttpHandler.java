package practicum.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import practicum.tasks.Endpoint;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

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

    protected void sendText(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        byte[] response = responseString.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(responseCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange, String text) throws IOException {

    }

    protected void sendHasInteractions() {

    }

    private static boolean isInteger(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches("-?\\d+(\\.\\d+)?");
    }

    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 3 && isInteger(pathParts[2])) {
            return Endpoint.GET_ID;
        }
        if (requestMethod.equals("GET")) {
            return Endpoint.GET_ALL;
        }
        if (requestMethod.equals("POST")) {
            return Endpoint.POST;
        }
        if (requestMethod.equals("DELETE")) {
            return Endpoint.DELETE;
        }
        return Endpoint.UNKNOWN;
    }
}
