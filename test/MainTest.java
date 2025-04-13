import java.util.List;
import practicum.managers.InMemoryTaskManager;
import practicum.tasks.Epic;
import practicum.tasks.Subtask;
import practicum.tasks.Task;
import practicum.tasks.TaskStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// ТЕСТЫ создания объектов (задач, эпиков, подзадач)
public class MainTest {
    static InMemoryTaskManager tm = new InMemoryTaskManager();
    static int idTask, idEpic, idSubtask;

    @BeforeAll
    static void createNewObjects() {
        idTask = 1001;
        tm.createTask(new Task("Test addNewTask", "Test addNewTask description", idTask, TaskStatus.NEW));

        idEpic = 1002;
        tm.createEpic(new Epic("Test addNewTask", "Test addNewTask description", idEpic, TaskStatus.NEW));

        idSubtask = 1003;
        tm.createSubtask(new Subtask("Test addNewTask", "Test addNewTask description", idEpic+1, idSubtask, TaskStatus.NEW));
    }

    @Test
    public void addNewTask() {
        System.out.println("==== Тест создания задачи ===============================================================");
        // Похоже я неправильно в 4-м спринте выбрал тип метода при создании объекта.
        // Надо было выбирать не void, а int для возвращения идентификатора созданного объекта
        final Task task = tm.getTaskById(idTask+1);
        final Task savedTask = tm.getTaskById(idTask+1);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = tm.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addNewEpic() {
        System.out.println("==== Тест создания эпика ================================================================");
        final Epic epic = tm.getEpicById(idEpic+1);
        final Epic savedEpic = tm.getEpicById(idEpic+1);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = tm.getAllEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }


    @Test
    void addNewSubtask() {
        System.out.println("==== Тест создания подзадачи ============================================================");
        final Subtask subtask = tm.getSubtaskById(idSubtask+1);
        final Subtask savedSubtask = tm.getSubtaskById(idSubtask+1);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final List<Subtask> subtasks = tm.getAllSubtasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }
}

// ToDo:
// 1. Переработать методы создания объектов в части возвращения идентификатора объекта (?)