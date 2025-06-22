package practicum.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Subtask extends Task {
    private int epicId; // Id родительского эпика

    public Subtask(String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime,
                   int epicId) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formattedStartDate = startTime.format(formatter);
        String formattedEndDate = getEndTime().format(formatter);

        return "SUBTASK[" + id + "]: " +
                String.format("%-40s", name + " (" + description + ").") +
                String.format("%-20s", " Статус " + status + ".") +
                " Продолжительность " + duration.toMinutes() + " мин. Дата старта " + formattedStartDate + "." +
                " Дата окончания " + formattedEndDate + "." +
                " ID эпика: " + epicId;
    }
}
