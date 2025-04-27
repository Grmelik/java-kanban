import practicum.managers.Managers;
import practicum.managers.TaskManager;
import practicum.tasks.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager tm = Managers.getDefault();

        // Тестирование
        // Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        Task task1 = new Task("Работа", "Сформировать отчет", TaskStatus.NEW);
        Task task2 = new Task("Дом", "Поклеить новые обои", TaskStatus.NEW);
        tm.createTask(task1);
        tm.createTask(task2);
        Epic epic1 = new Epic("Путешествие", "Подготовка к поездке", TaskStatus.NEW);
        Subtask subtask1 = new Subtask("Билеты", "Купить билеты", TaskStatus.NEW);
        Subtask subtask2 = new Subtask("Отели", "Забронировать отели", TaskStatus.NEW);
        tm.createEpic(epic1);
        tm.createSubtask(subtask1, epic1.getId());
        tm.createSubtask(subtask2, epic1.getId());
        Epic epic2 = new Epic("Обучение", "Курс Java", TaskStatus.NEW);
        Subtask subtask3 = new Subtask("Спринт 4", "Сдать задание 4-го спринта", TaskStatus.NEW);
        tm.createEpic(epic2);
        tm.createSubtask(subtask3, epic2.getId());

        // Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
        System.out.println('\n' + "== Просмотр событий (задачи, эпики, подзадачи, история) ==========================");
        tm.printAllTasks();

        // Измените статусы созданных объектов, распечатайте их.
        // Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        System.out.println('\n' + "== Измените статусы созданных объектов, распечатайте их ==========================");
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
        subtask3.setName("Спринт 5");
        subtask3.setDescription("Сдать задание 5-го спринта");
        subtask3.setStatus(TaskStatus.DONE);
        tm.updateSubtask(subtask3);
        epic1.setName("Командировка");
        tm.updateEpic(epic1);
        tm.updateEpic(epic2);
        tm.printAllTasks();

        // Проверка истории
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

        // И, наконец, попробуйте удалить одну из задач и один из эпиков.
        System.out.println('\n' + "== Удалить одну из задач и один из эпиков ========================================");
        tm.deleteTaskById(task1.getId());
        tm.deleteEpicById(epic2.getId());
        //tm.deleteAllTasks();
        //tm.deleteAllEpics();
        tm.printAllTasks();
        tm.printAllHistory();
    }
}
