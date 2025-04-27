package practicum.managers;

import practicum.tasks.Task;
import java.util.List;

// Интерфейс для управления историей просмотров
public interface HistoryManager {
    // Помечаем задачи как просмотреные
    void add(Task task);

    // Удаление задачи из просмотра
    void remove(int id);

    // Последние 10 просмотренных задач
    List<Task> getHistory();
}
