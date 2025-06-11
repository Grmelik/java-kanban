package practicum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practicum.managers.InMemoryTaskManager;
import practicum.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest <InMemoryTaskManager> {

    @BeforeEach
    public void prepareTests()  {
        tm = new InMemoryTaskManager();
        tm.deleteAllEpics();
        tm.deleteAllTasks();
        tm.clearingTaskSet();
        createNewObjects();
    }

    @Test
    void testCreateNewObjects() {
        System.out.println("==== Проверка создания объектов (задача, эпик, подзадача) ===============================");
        Assertions.assertNotNull(tm.getTaskById(idTask), "Ошибка создания задачи");
        Assertions.assertNotNull(tm.getSubtaskById(idSubtask1), "Ошибка создания подзадачи1");
        Assertions.assertNotNull(tm.getSubtaskById(idSubtask2), "Ошибка создания подзадачи2");
    }

    @Test
    void testGetAllTasks() {
        System.out.println("==== Проверка наличия задач в списке ====================================================");
        List<Task> allTasks = tm.getAllTasks();
        assertFalse(allTasks.isEmpty(), "В списке должны содержаться задачи");
    }

    @Test
    void testGetAllEpics() {
        System.out.println("==== Проверка наличия эпиков в списке ===================================================");
        List<Epic> allEpics = tm.getAllEpics();
        assertFalse(allEpics.isEmpty(), "В списке должны содержаться эпики");
    }

    @Test
    void testGetAllSubtasks() {
        System.out.println("==== Проверка наличия подзадач в списке =================================================");
        List<Subtask> allSubtasks = tm.getAllSubtasks();
        assertFalse(allSubtasks.isEmpty(), "В списке должны содержаться подзадачи");
    }

    @Test
    void testEpicBoundaryConditions() {
        System.out.println("==== Проверка граничных условий эпика (Все подзадачи со статусом NEW) ===================");
        Assertions.assertEquals(TaskStatus.NEW, tm.getEpicById(idEpic).getStatus(),
                "У подзадач статус = NEW, и у эпика должен быть статус NEW");

        System.out.println("==== Проверка граничных условий эпика (Все подзадачи со статусом DONE) ==================");
        tm.getSubtaskById(idSubtask1).setStatus(TaskStatus.DONE);
        tm.getSubtaskById(idSubtask2).setStatus(TaskStatus.DONE);
        tm.updateSubtask(tm.getSubtaskById(idSubtask1));
        tm.updateSubtask(tm.getSubtaskById(idSubtask2));
        Assertions.assertEquals(TaskStatus.DONE, tm.getEpicById(idEpic).getStatus(),
                "У подзадач статус = DONE, и у эпика должен быть статус DONE");

        System.out.println("==== Проверка граничных условий эпика (Подзадачи со статусами NEW и DONE) ===============");
        tm.getSubtaskById(idSubtask1).setStatus(TaskStatus.NEW);
        tm.getSubtaskById(idSubtask2).setStatus(TaskStatus.DONE);
        tm.updateSubtask(tm.getSubtaskById(idSubtask1));
        tm.updateSubtask(tm.getSubtaskById(idSubtask2));
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, tm.getEpicById(idEpic).getStatus(),
                "У подзадач статусы NEW и DONE, и у эпика должен быть статус IN_PROGRESS");

        System.out.println("==== Проверка граничных условий эпика (Подзадачи со статусом IN_PROGRESS) ===============");
        tm.getSubtaskById(idSubtask1).setStatus(TaskStatus.NEW);
        tm.getSubtaskById(idSubtask2).setStatus(TaskStatus.IN_PROGRESS);
        tm.updateSubtask(tm.getSubtaskById(idSubtask1));
        tm.updateSubtask(tm.getSubtaskById(idSubtask2));
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, tm.getEpicById(idEpic).getStatus(),
                "У подзадач статусы NEW и IN_PROGRESS, и у эпика должен быть статус IN_PROGRESS");
    }

    @Test
    void testIntersection() throws Exception {
        System.out.println("==== Проверка пересечения интервалов ====================================================");
        // В списке приоритетов уже есть 3 задачи (task, subtask1, subtask2)
        Assertions.assertEquals(3, tm.getPrioritizedTasks().size(), "В списке должно быть три задачи");

        Task task2 = new Task("Дом", "Поклеить новые обои", TaskStatus.NEW,
                Duration.ofMinutes(45), LocalDateTime.of(2022, 5, 20, 10, 10));
        tm.createTask(task2);
        // Пересечения по времени нет, следовательно, в список добавляется новая задача task2
        Assertions.assertEquals(4, tm.getPrioritizedTasks().size(), "В списке должно быть четыре задачи");
    }

    @Test
    void testDeleteObjects() {
        System.out.println("==== Проверка удаления объектов (задача, эпик, подзадача) ===============================");
        tm.deleteTaskById(idTask);
        tm.deleteEpicById(idEpic);
        tm.deleteSubtaskById(idSubtask1);
        tm.deleteSubtaskById(idSubtask2);
        Assertions.assertNull(tm.getTaskById(idTask), "Ошибка удаления задачи");
        Assertions.assertNull(tm.getEpicById(idEpic), "Ошибка удаления эпика");
        Assertions.assertNull(tm.getSubtaskById(idSubtask1), "Ошибка удаления подзадачи1");
        Assertions.assertNull(tm.getSubtaskById(idSubtask2), "Ошибка удаления подзадачи2");
    }

    @AfterEach
    void completeTests() {
        tm.deleteAllEpics();
        tm.deleteAllTasks();
        tm.clearingTaskSet();
    }
}
