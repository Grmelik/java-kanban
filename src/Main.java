import practicum.managers.InMemoryTaskManager;
import practicum.tasks.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        InMemoryTaskManager tm = new InMemoryTaskManager();

        // Тестирование
        // Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        tm.createTask(new Task("Работа", "Сформировать отчет", tm.generateId(), TaskStatus.NEW));                 //1002
        tm.createTask(new Task("Дом", "Поклеить новые обои", tm.generateId(), TaskStatus.NEW));                   //1003
        tm.createEpic(new Epic("Путешествие", "Подготовка к поездке", tm.generateId(), TaskStatus.NEW));          //1004
        tm.createSubtask(new Subtask("Билеты", "Купить билеты", 1004, tm.generateId(), TaskStatus.NEW));          //1005
        tm.createSubtask(new Subtask("Отели", "Забронировать отели", 1004, tm.generateId(), TaskStatus.NEW));     //1006
        tm.createEpic(new Epic("Обучение", "Курс Java", tm.generateId(), TaskStatus.NEW));                        //1007
        tm.createSubtask(new Subtask("Спринт 4", "Сдать задание 4-го спринта", 1007, tm.generateId(), TaskStatus.NEW)); //1008

        // Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
        System.out.println('\n' + "== Просмотр событий (задачи, эпики, подзадачи, история) ==========================");
        tm.printAllTasks(tm);

        // Измените статусы созданных объектов, распечатайте их.
        // Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        System.out.println('\n' + "== Измените статусы созданных объектов, распечатайте их ==========================");
        // Задача 1 с Id=1002
        Task task1 = tm.getTaskById(1002);
        task1.setName("Печать");
        task1.setDescription("Распечатать отчет");
        task1.setStatus(TaskStatus.DONE);
        tm.updateTask(task1);
        // Задача 2 с Id=1003
        Task task2 = tm.getTaskById(1003);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateTask(task2);
        // Подзадача 1 с Id=1005
        Subtask subtask1 = tm.getSubtaskById(1005);
        subtask1.setName("Билеты на поезд");
        subtask1.setDescription("Купить билеты в купе");
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateSubtask(subtask1);
        // Подзадача 2 с Id=1006
        Subtask subtask2 = tm.getSubtaskById(1006);
        subtask2.setDescription("Забронировать и оплатить отели");
        subtask2.setStatus(TaskStatus.DONE);
        tm.updateSubtask(subtask2);
        // Подзадача 3 с Id=1008
        Subtask subtask3 = tm.getSubtaskById(1008);
        subtask3.setStatus(TaskStatus.DONE);
        tm.updateSubtask(subtask3);
        // Эпик 1 с Id=1004
        Epic epic1 = tm.getEpicById(1004);
        epic1.setName("Командировка");
        tm.updateEpic(epic1);
        // Эпик 2 с Id=1007
        Epic epic2 = tm.getEpicById(1007);
        tm.updateEpic(epic2);
        tm.printAllTasks(tm);

        // И, наконец, попробуйте удалить одну из задач и один из эпиков.
        System.out.println('\n' + "== Удалить одну из задач и один из эпиков ========================================");
        tm.deleteTaskById(1002);
        tm.deleteEpicById(1007);
        //tm.deleteAllTasks();
        //tm.deleteAllEpics();
        tm.printAllTasks(tm);
    }
}
