package practicum.managers;

import practicum.tasks.Task;
import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> historyList = new ArrayList<>();
    private static final int HISTORY_LIST_SIZE = 10;

    @Override
    public void add(Task task) {
        Task historyTask = new Task(task.getName(), task.getDescription(), task.getStatus());
        historyTask.setId(task.getId());
        if (historyList.size() >= HISTORY_LIST_SIZE) {
            historyList.removeFirst();
        }
        historyList.add(historyTask);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyList);
    }
}
