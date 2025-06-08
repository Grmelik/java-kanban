package practicum.support;

import practicum.managers.TaskManager;
import practicum.tasks.Epic;
import practicum.tasks.Subtask;
import practicum.tasks.Task;
import practicum.tasks.TaskStatus;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

public class Support {

    public static void testMemory(TaskManager tm) {
        tm.clearingTaskSet();
        System.out.println("== Тестирование (Создаём 2 задачи, эпик с 2 подзадачами и эпик с одной задачей) =========");
        Task task1 = new Task("Работа", "Сформировать отчет", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 5, 20, 10, 00));
        Task task2 = new Task("Дом", "Поклеить новые обои", TaskStatus.NEW,
                Duration.ofMinutes(45), LocalDateTime.of(2025, 5, 22, 12, 30));
        tm.createTask(task1);
        tm.createTask(task2);
        Epic epic1 = new Epic("Путешествие", "Подготовка к поездке", TaskStatus.NEW,
                Duration.ofMinutes(0), LocalDateTime.now());
        Subtask subtask1 = new Subtask("Билеты", "Купить билеты", TaskStatus.NEW,
                Duration.ofMinutes(300), LocalDateTime.of(2025, 2, 21, 9, 15));
        Subtask subtask2 = new Subtask("Отели", "Забронировать отели", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2025, 2, 15, 19, 33));
        tm.createEpic(epic1);
        tm.createSubtask(subtask1, epic1.getId());
        tm.createSubtask(subtask2, epic1.getId());
        Epic epic2 = new Epic("Обучение", "Курс Java", TaskStatus.NEW,
                Duration.ofMinutes(0), LocalDateTime.now());
        Subtask subtask3 = new Subtask("Спринт 4", "Сдать задание 4-го спринта", TaskStatus.NEW,
                Duration.ofMinutes(480), LocalDateTime.of(2025, 4, 10, 15, 15));
        tm.createEpic(epic2);
        tm.createSubtask(subtask3, epic2.getId());

        System.out.println('\n' + "== Результаты создания ===========================================================");
        tm.printAllTasks();

        System.out.println('\n' + "== Изменение статусов созданных объектов =========================================");
        task1.setName("Печать");
        task1.setDescription("Распечатать отчет");
        task1.setStatus(TaskStatus.DONE);
        tm.updateTask(task1);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateTask(task2);
        subtask1.setName("Билеты на поезд");
        subtask1.setDescription("Купить билеты в купе");
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateSubtask(subtask1);
        subtask2.setDescription("Забронировать и оплатить отели");
        subtask2.setStatus(TaskStatus.DONE);
        tm.updateSubtask(subtask2);
        subtask3.setName("Спринт 8");
        subtask3.setDescription("Сдать задание 8-го спринта");
        subtask3.setStatus(TaskStatus.DONE);
        tm.updateSubtask(subtask3);
        epic1.setName("Командировка");
        tm.updateEpic(epic1);
        tm.updateEpic(epic2);
        tm.printAllTasks();

        System.out.println('\n' + "== Просмотр истории через вызов тасков, эпиков и подзадач ========================");
        tm.getSubtaskById(subtask1.getId());
        tm.getTaskById(task1.getId());
        tm.getEpicById(epic1.getId());
        tm.getSubtaskById(subtask1.getId());
        tm.getTaskById(task2.getId());
        tm.getEpicById(epic2.getId());
        tm.getSubtaskById(subtask2.getId());
        tm.getSubtaskById(subtask3.getId());
        tm.getTaskById(task2.getId());
        tm.getSubtaskById(subtask2.getId());
        tm.printAllHistory();

        System.out.println('\n' + "== Удаление одной задачи и одного эпика ==========================================");
        tm.deleteTaskById(task1.getId());
        tm.deleteEpicById(epic2.getId());
        //tm.deleteAllTasks();
        //tm.deleteAllEpics();
        tm.printAllTasks();
        tm.printAllHistory();
    }

    public static void testFile(TaskManager tm) {
        tm.clearingTaskSet();
        Task task1 = new Task("Работа", "Сформировать отчет", TaskStatus.NEW
                , Duration.ofMinutes(30), LocalDateTime.of(2025, 5, 20, 10, 10));
        Task task2 = new Task("Дом", "Поклеить новые обои", TaskStatus.NEW,
                Duration.ofMinutes(45), LocalDateTime.of(2025, 5, 22, 12, 40));
        tm.createTask(task1);
        tm.createTask(task2);
        Epic epic1 = new Epic("Путешествие", "Подготовка к поездке", TaskStatus.NEW,
                Duration.ofMinutes(0), LocalDateTime.now());
        Subtask subtask1 = new Subtask("Билеты", "Купить билеты", TaskStatus.NEW,
                Duration.ofMinutes(300), LocalDateTime.of(2025, 2, 21, 9, 25));
        Subtask subtask2 = new Subtask("Отели", "Забронировать отели", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2025, 2, 15, 19, 43));
        tm.createEpic(epic1);
        tm.createSubtask(subtask1, epic1.getId());
        tm.createSubtask(subtask2, epic1.getId());
        Epic epic2 = new Epic("Обучение", "Курс Java", TaskStatus.NEW,
                Duration.ofMinutes(0), LocalDateTime.now());
        Subtask subtask3 = new Subtask("Спринт 4", "Сдать задание 4-го спринта", TaskStatus.NEW,
                Duration.ofMinutes(480), LocalDateTime.of(2025, 4, 10, 15, 25));
        tm.createEpic(epic2);
        tm.createSubtask(subtask3, epic2.getId());

        task1.setName("Печать");
        task1.setDescription("Распечатать отчет");
        task1.setStatus(TaskStatus.DONE);
        tm.updateTask(task1);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateTask(task2);
        subtask1.setName("Билеты на поезд");
        subtask1.setDescription("Купить билеты в купе");
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateSubtask(subtask1);
        subtask2.setDescription("Забронировать и оплатить отели");
        subtask2.setStatus(TaskStatus.DONE);
        tm.updateSubtask(subtask2);
        subtask3.setName("Спринт 8");
        subtask3.setDescription("Сдать задание 8-го спринта");
        subtask3.setStatus(TaskStatus.DONE);
        tm.updateSubtask(subtask3);
        epic1.setName("Командировка");
        tm.updateEpic(epic1);
        tm.updateEpic(epic2);
    }

    public static boolean checkFile(String dirname, String filename) {
        return Files.exists(Paths.get(dirname, filename));
    }
}
