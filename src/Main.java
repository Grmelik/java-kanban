public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager tm = new TaskManager();

        // Тестирование
        // Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        tm.createTask(new Task("Работа", "Сформировать отчет", TaskStatus.NEW));  //1001
        tm.createTask(new Task("Дом", "Поклеить новые обои", TaskStatus.NEW));    //1002
        tm.createEpic(new Epic("Путешествие", "Подготовка к поездке", TaskStatus.NEW));    //1003
        tm.createSubtask(new Subtask("Билеты", "Купить билеты", tm.epicId, TaskStatus.NEW)); //1004
        tm.createSubtask(new Subtask("Отели", "Забронировать отели", tm.epicId, TaskStatus.NEW));  //1005
        tm.createEpic(new Epic("Обучение", "Курс Java", TaskStatus.NEW));   //1006
        tm.createSubtask(new Subtask("Спринт 4", "Сдать задание 4-го спринта", tm.epicId, TaskStatus.NEW)); //1007

        // Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
        System.out.println('\n' + "== Распечатайте списки эпиков, задач и подзадач ==================================");
        tm.getListOfAllTasks();
        tm.getListOfAllEpics();

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
        tm.getListOfAllTasks();
        tm.getListOfAllEpics();

        // И, наконец, попробуйте удалить одну из задач и один из эпиков.
        System.out.println('\n' + "== Удалить одну из задач и один из эпиков ========================================");
        tm.deleteTaskById(1001);
        tm.deleteEpicById(1006);
        tm.getListOfAllTasks();
        tm.getListOfAllEpics();
    }
}
