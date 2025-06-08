package practicum;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import practicum.managers.HistoryManager;
import practicum.managers.InMemoryTaskManager;
import practicum.managers.Managers;
import practicum.tasks.Task;
import practicum.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

// ТЕСТ добавления в историю
class InMemoryHistoryManagerTest {
    static InMemoryTaskManager tm = new InMemoryTaskManager();
    static HistoryManager hm = Managers.getDefaultHistory();
    static int idTask;

    @Test
    void testAddingInHistory() {
        System.out.println("==== Тест добавления в историю ==========================================================");
        Task task1 = new Task("Дом", "Поклеить новые обои", TaskStatus.NEW,
                Duration.ofMinutes(45), LocalDateTime.of(2025, 5, 22, 12, 30));
        tm.createTask(task1);
        hm.add(task1);
        final List<Task> history = hm.getHistory();
        assertNotNull(history, "1.После добавления задачи, история не должна быть пустой.");
        assertEquals(1, history.size(), "2.После добавления задачи, история не должна быть пустой.");
    }

    @Test
    void testHistoryIsEmpty() {
        System.out.println("==== Тест пустой истории ================================================================");
        assertTrue(hm.getHistory().isEmpty(), "История не пустая");
    }

    @Test
    void testHistoryIsDuplicate() {
        System.out.println("==== Тест дублирования истории ==========================================================");
        Task task1 = new Task("Дом", "Поклеить новые обои", TaskStatus.NEW,
                Duration.ofMinutes(45), LocalDateTime.of(2025, 5, 22, 12, 30));
        Task task2 = new Task("Дом2", "Поклеить новые обои", TaskStatus.NEW,
                Duration.ofMinutes(45), LocalDateTime.of(2025, 5, 22, 12, 30));
        tm.createTask(task1);
        hm.add(task1);
        hm.add(task2);

        String[] str = new String[2];
        int i = 0;
        for (Task task : hm.getHistory()) {
            str[i] = task.getName() + ", " + task.getDescription() + ", " + task.getStatus() + ", " + task.getDuration() +
                    ", " + task.getStartTime();
            i++;
        }
        assertNotEquals(str[0], str[1], "Дублирование значений в истории");
    }

    @Test
    void testHistoryIsDeleteBegin() {
        System.out.println("==== Тест удаления из истории (начало) ==================================================");
        Task task1 = new Task("Дом", "Поклеить новые обои", TaskStatus.NEW,
                Duration.ofMinutes(45), LocalDateTime.of(2025, 5, 22, 12, 30));
        Task task2 = new Task("Работа", "Сформировать отчет", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 5, 20, 10, 00));
        tm.createTask(task1);
        tm.createTask(task2);
        hm.add(task1);
        hm.add(task2);

        hm.remove(task1.getId());
        assertFalse(hm.getHistory().contains(task1), "Задача 1 не удалена из истории");
    }

    @Test
    void testHistoryIsDeleteMiddle() {
        System.out.println("==== Тест удаления из истории (середина) ================================================");
        Task task1 = new Task("Дом", "Поклеить новые обои", TaskStatus.NEW,
                Duration.ofMinutes(45), LocalDateTime.of(2025, 5, 22, 12, 30));
        Task task2 = new Task("Работа", "Сформировать отчет", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 5, 20, 10, 00));
        Task task3 = new Task("Учеба", "Сформировать отчет", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 2, 20, 10, 00));
        tm.createTask(task1);
        tm.createTask(task2);
        tm.createTask(task3);
        hm.add(task1);
        hm.add(task2);
        hm.add(task3);

        hm.remove(task2.getId());
        assertFalse(hm.getHistory().contains(task2), "Задача 2 не удалена из истории");
    }

    @Test
    void testHistoryIsDeleteEnd() {
        System.out.println("==== Тест удаления из истории (конец) ===================================================");
        Task task1 = new Task("Дом", "Поклеить новые обои", TaskStatus.NEW,
                Duration.ofMinutes(45), LocalDateTime.of(2025, 5, 22, 12, 30));
        Task task2 = new Task("Работа", "Сформировать отчет", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 5, 20, 10, 00));
        tm.createTask(task1);
        tm.createTask(task2);
        hm.add(task1);
        hm.add(task2);

        hm.remove(task2.getId());
        assertFalse(hm.getHistory().contains(task2), "Задача 2 не удалена из истории");
    }

    @AfterEach
    void completeTests() {
        tm.deleteAllEpics();
        tm.deleteAllTasks();
        tm.clearingTaskSet();
        hm.clearAllHistory();
    }
}
