package practicum.tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksList = new ArrayList<>();

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    // Получить список подзадач
    public ArrayList<Integer> getSubtasksList() {
        return subtasksList;
    }

    // Добавить подзадачу в список подзадач по идентификатору подзадачи
    public void addSubtask(int subtaskId) {
        if (subtaskId != getId()) {
            subtasksList.add(subtaskId);
        }
    }

    // Очистить список подзадач по индексу
    public void clearSubtasksList(int index) {
        if (!subtasksList.isEmpty()) {
            subtasksList.remove(index);
        }
    }

    // Очистить список задач
    public void clearSubtasksList() {
        if (!subtasksList.isEmpty()) {
            subtasksList.clear();
        }
    }

    /*@Override
    public String toString() {
        return "ID=[" + id + "]: " + name + " (" + description + "). Статус " + status + ".";
    }*/
}
