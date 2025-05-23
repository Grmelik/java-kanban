package practicum.tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksList = new ArrayList<>();

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubtasksList() {
        return subtasksList;
    }

    public void addSubtask(int subtaskId) {
        if (subtaskId != getId()) {
            subtasksList.add(subtaskId);
        }
    }

    public void clearSubtasksList(int index) {
        if (!subtasksList.isEmpty()) {
            subtasksList.remove(index);
        }
    }

    public void clearSubtasksList() {
        if (!subtasksList.isEmpty()) {
            subtasksList.clear();
        }
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }
}
