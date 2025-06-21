package practicum;

import practicum.managers.TaskManager;
import practicum.tasks.Epic;
import practicum.tasks.Subtask;
import practicum.tasks.Task;
import practicum.tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected static TaskManager tm;
    protected static int idTask, idEpic, idSubtask1, idSubtask2;

    //@BeforeEach
    //protected abstract void prepareTests() throws IOException;

    static void createNewObjects() {
        Task task1 = new Task("Работа", "Сформировать отчет", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 5, 20, 10, 00));
        Epic epic1 = new Epic("Путешествие", "Подготовка к поездке", TaskStatus.NEW,
                Duration.ofMinutes(0), LocalDateTime.now(), List.of());

        tm.createTask(task1);
        tm.createEpic(epic1);
        idTask = task1.getId();
        idEpic = epic1.getId();

        Subtask subtask1 = new Subtask("Билеты", "Купить билеты", TaskStatus.NEW, Duration.ofMinutes(300),
                LocalDateTime.of(2025, 2, 21, 9, 15), idEpic);
        Subtask subtask2 = new Subtask("Отели", "Забронировать отели", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2025, 2, 15, 19, 33), idEpic);

        tm.createSubtask(subtask1);
        tm.createSubtask(subtask2);
        idSubtask1 = subtask1.getId();
        idSubtask2 = subtask2.getId();
    }
}
