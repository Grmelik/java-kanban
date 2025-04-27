package practicum.managers;

import practicum.tasks.Epic;
import practicum.tasks.Subtask;
import practicum.tasks.Task;
import java.util.List;

public interface TaskManager {
    // a.Получение списка всех задач
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    // b.Удаление всех задач
    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    // c.Получение по идентификатору
    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    // d.Создание. Сам объект должен передаваться в качестве параметра
    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask, int epicId);

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    // f. Удаление по идентификатору.
    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    // g. Получение списка всех подзадач определённого эпика.
    List<Subtask> getSubtasksOfEpic(int epicId);

    List<Task> getHistory();

    void printAllTasks();

    void printAllHistory();
}
