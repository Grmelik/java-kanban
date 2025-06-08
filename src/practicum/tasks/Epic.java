package practicum.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksList = new ArrayList<>();
    protected LocalDateTime endTime;

    public Epic(String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.endTime = LocalDateTime.now();
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

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
