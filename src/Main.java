import practicum.managers.Tracker;
import practicum.tasks.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Tracker tm = new Tracker();

        // Тестирование
        // Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        tm.createTask(new Task("Работа", "Сформировать отчет", tm.generateId(), TaskStatus.NEW));                 //1001
        tm.createTask(new Task("Дом", "Поклеить новые обои", tm.generateId(), TaskStatus.NEW));                   //1002
        tm.createEpic(new Epic("Путешествие", "Подготовка к поездке", tm.generateId(), TaskStatus.NEW));          //1003
        tm.createSubtask(new Subtask("Билеты", "Купить билеты", tm.generateId(), TaskStatus.NEW));                //1004
        tm.createSubtask(new Subtask("Отели", "Забронировать отели", tm.generateId(), TaskStatus.NEW));           //1005
        tm.createEpic(new Epic("Обучение", "Курс Java", tm.generateId(), TaskStatus.NEW));                        //1006
        tm.createSubtask(new Subtask("Спринт 4", "Сдать задание 4-го спринта", tm.generateId(), TaskStatus.NEW)); //1007

        // Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
        System.out.println('\n' + "== Распечатайте списки эпиков, задач и подзадач ==================================");
        System.out.println(tm.getAllTasks());
        System.out.println(tm.getAllEpics());
        System.out.println(tm.getAllSubtasks());

        // Измените статусы созданных объектов, распечатайте их.
        // Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        System.out.println('\n' + "== Измените статусы созданных объектов, распечатайте их ==========================");
        tm.updateTask(tm.getTaskById(1001));
        tm.updateTask(tm.getTaskById(1001));
        tm.updateTask(tm.getTaskById(1002));
        tm.updateSubtask(tm.getSubtaskById(1004));
        tm.updateSubtask(tm.getSubtaskById(1005));
        tm.updateSubtask(tm.getSubtaskById(1005));
        tm.updateSubtask(tm.getSubtaskById(1007));
        tm.updateSubtask(tm.getSubtaskById(1007));
        tm.updateEpic(tm.getEpicById(1003));
        tm.updateEpic(tm.getEpicById(1006));
        System.out.println(tm.getAllTasks());
        System.out.println(tm.getAllEpics());
        System.out.println(tm.getAllSubtasks());

        // И, наконец, попробуйте удалить одну из задач и один из эпиков.
        System.out.println('\n' + "== Удалить одну из задач и один из эпиков ========================================");
        tm.deleteTaskById(1001);
        tm.deleteEpicById(1006);
        //tm.deleteAllTasks();
        //tm.deleteAllEpics();
        System.out.println(tm.getAllTasks());
        System.out.println(tm.getAllEpics());
        System.out.println(tm.getAllSubtasks());
    }
}
