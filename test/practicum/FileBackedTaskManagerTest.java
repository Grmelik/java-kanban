package practicum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practicum.managers.FileBackedTaskManager;
import practicum.managers.Managers;
import practicum.managers.TaskManager;
import practicum.tasks.*;

import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest <FileBackedTaskManager> {
    String dirname = System.getProperty("user.home");
    String filename = "backed_test.csv";
    TaskManager fb = Managers.getDefaultFileBacked(dirname, filename);

    @BeforeEach
    public void prepareTests()  {
        Task task1 = new Task("Работа", "Сформировать отчет", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 5, 20, 10, 00));
        Epic epic1 = new Epic("Путешествие", "Подготовка к поездке", TaskStatus.NEW,
                Duration.ofMinutes(0), LocalDateTime.now());
        Subtask subtask1 = new Subtask("Билеты", "Купить билеты", TaskStatus.NEW,
                Duration.ofMinutes(300), LocalDateTime.of(2025, 2, 21, 9, 15));
        Subtask subtask2 = new Subtask("Отели", "Забронировать отели", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2025, 2, 15, 19, 33));
        fb.createTask(task1);
        fb.createEpic(epic1);
        fb.createSubtask(subtask1, epic1.getId());
        fb.createSubtask(subtask2, epic1.getId());
    }

    @Test
    void testSaveInFile() {
        System.out.println("==== Тест сохранения в файл =============================================================");
        assertNotNull(filename, "Файл " + filename + " не создан");
    }

    @Test
    void testLoadFromFile() {
        System.out.println("==== Тест загрузки из файла =============================================================");
        FileBackedTaskManager fb = Managers.getDefaultFileBacked();
        fb.loadFromFile(Paths.get(dirname, filename).toFile());
        assertNotNull(fb.getTasksFromFile(), "Список задач, загруженных из файла не должен быть пустым");
    }

    @AfterEach
    void completeTests() {
        fb.deleteAllEpics();
        fb.deleteAllTasks();
        fb.clearingTaskSet();
    }
}
