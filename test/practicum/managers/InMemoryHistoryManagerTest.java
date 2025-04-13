package practicum.managers;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import practicum.tasks.Task;
import practicum.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;
import static practicum.managers.InMemoryTaskManager.historyManager;

// ТЕСТ добавления в историю
class InMemoryHistoryManagerTest {
    static InMemoryTaskManager tm = new InMemoryTaskManager();
    static int idTask = 1001;

    @BeforeAll
    static void createNewTask() {
        tm.createTask(new Task("Test addNewTaskNo2", "Test addNewTaskNo2 description", idTask, TaskStatus.NEW));
    }

    @Test
    void add() {
        System.out.println("==== Тест добавления в историю===========================================================");
        final Task task = tm.getTaskById(idTask+1);

        //historyManager.add(task); // уже вызывается выше в методе tm.getTaskById
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "1.После добавления задачи, история не должна быть пустой.");
        assertEquals(1, history.size(), "2.После добавления задачи, история не должна быть пустой.");
    }
}

// ToDo:
// 1. Переработать методы создания объектов в части возвращения идентификатора объекта (?)