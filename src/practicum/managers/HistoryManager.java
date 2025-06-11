package practicum.managers;

import java.util.List;
import practicum.tasks.Task;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();

    void clearAllHistory();
}
