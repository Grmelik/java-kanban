package practicum.tasks;

import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> subtasksList = new ArrayList<>();

    public Epic(String name, String description, int id, TaskStatus status) {
        super(name, description, id, status);
    }

    // Получить список подзадач
    public ArrayList<Integer> getSubtasksList() {
        return subtasksList;
    }

    // Добавить подзадачу в список подзадач по идентификатору подзадачи
    public void addSubtask(int subtaskId) {
        subtasksList.add(subtaskId);
    }

    // Очистить список подзадач по индексу
    public void clearSubtasksList(int index) {
        if (!subtasksList.isEmpty()) {
            subtasksList.remove(index);
        }
    }

    // Очистить список задач
    public void clearSubtasksList() {
        if (!subtasksList.isEmpty()){
            subtasksList.clear();
        }
    }

    @Override
    public String toString() {
        return '\n' + "ID=[" + id + "] Эпик : " + name + " (" + description + "). Статус " + status +
                ". Список подзадач = " + subtasksList;
    }
}
