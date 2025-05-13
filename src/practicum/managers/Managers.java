package practicum.managers;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultFileBacked(String dirname, String filename) {
        return new FileBackedTaskManager(dirname, filename);
    }

    public static FileBackedTaskManager getDefaultFileBacked() {
        return new FileBackedTaskManager();
    }
}
