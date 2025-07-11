package practicum.managers;

import practicum.tasks.Epic;
import practicum.tasks.Subtask;
import practicum.tasks.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    List<Subtask> getSubtasksOfEpic(int epicId);

    List<Task> getHistory();

    void printAllTasks();

    void printAllHistory();

    void printAllPrioritizedTasks();

    void printIterator();

    Set<Task> getPrioritizedTasks();

    void clearingTaskSet();

}
