package practicum.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksList;
    protected LocalDateTime endTime;

    public Epic(String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime,
                List<Integer> subtasksList) {
        super(name, description, status, duration, startTime);
        this.endTime = LocalDateTime.now();
        this.subtasksList = new ArrayList<>(subtasksList);
    }

    public List<Integer> getSubtasksList() {
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

    public void clearSubtasksList(Object o) {
        if (!subtasksList.isEmpty()) {
            subtasksList.remove(o);
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formattedStartDate = startTime.format(formatter);
        String formattedEndDate = getEndTime().format(formatter);

        return "EPIC[" + id + "]: " +
                String.format("%-40s", name + " (" + description + ").") +
                String.format("%-20s", " Статус " + status + ".") +
                " Продолжительность " + duration.toMinutes() + " мин. Дата старта " + formattedStartDate + "." +
                " Дата окончания " + formattedEndDate + "." +
                " Список подзадач: " + subtasksList;
    }
}
