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
        Task task1 = new Task("Работа", "Сформировать отчет", TaskStatus.NEW);
        tm.createTask(task1);
        idTask = task1.getId();

        Epic epic1 = new Epic("Путешествие", "Подготовка к поездке", TaskStatus.NEW);
        tm.createEpic(epic1);
        idEpic = epic1.getId();

        Subtask subtask1 = new Subtask("Билеты", "Купить билеты", TaskStatus.NEW);
        tm.createSubtask(subtask1, epic1.getId());
        idSubtask = subtask1.getId();
    }

    @Test
    public void addNewTask() {
        System.out.println("==== Тест создания задачи ===============================================================");
        // Похоже я неправильно в 4-м спринте выбрал тип метода при создании объекта.
        // Надо было выбирать не void, а int для возвращения идентификатора созданного объекта
        final Task task = tm.getTaskById(idTask);
        final Task savedTask = tm.getTaskById(idTask);

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
        final Epic epic = tm.getEpicById(idEpic);
        final Epic savedEpic = tm.getEpicById(idEpic);

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
        final Subtask subtask = tm.getSubtaskById(idSubtask);
        final Subtask savedSubtask = tm.getSubtaskById(idSubtask);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final List<Subtask> subtasks = tm.getAllSubtasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }
}
