package practicum.managers;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import practicum.tasks.Task;
import practicum.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

// ТЕСТ добавления в историю
class InMemoryHistoryManagerTest {
    static InMemoryTaskManager tm = new InMemoryTaskManager();
    static HistoryManager hm = Managers.getDefaultHistory();
    static int idTask;

    @BeforeAll
    static void createNewTask() {
        Task task2 = new Task("Дом", "Поклеить новые обои", TaskStatus.NEW);
        tm.createTask(task2);
        idTask = task2.getId();
    }

    @Test
    void add() {
        System.out.println("==== Тест добавления в историю===========================================================");
        final Task task = tm.getTaskById(idTask);

        hm.add(task);
        final List<Task> history = hm.getHistory();
        assertNotNull(history, "1.После добавления задачи, история не должна быть пустой.");
        assertEquals(1, history.size(), "2.После добавления задачи, история не должна быть пустой.");
    }
}
