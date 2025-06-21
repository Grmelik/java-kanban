package practicum.http;

import com.sun.net.httpserver.HttpServer;
import practicum.managers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final TaskManager taskManager;
    private static final int PORT = 8080;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void startServer() throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.start();
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.startServer();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }
}
