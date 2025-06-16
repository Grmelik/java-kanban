package practicum.http;

import com.sun.net.httpserver.HttpServer;
import practicum.managers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    //public TaskManager taskManager = Managers.getDefault();
    private final TaskManager taskManager;
    private static final int PORT = 8080;
    private static HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void startServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.start();
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.startServer();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }
}
