package practicum.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = -1;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && status == task.status;
    }

    @Override
    public int hashCode() {
        int result;
        result = 31 * Objects.hash(name, description, id, status);
        return result;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formattedStartDate = startTime.format(formatter);
        String formattedEndDate = getEndTime().format(formatter);

        return "[" + id + "]: " +
                String.format("%-40s", name + " (" + description + ").") +
                String.format("%-20s", " Статус " + status + ".") +
                " Продолжительность " + duration.toMinutes() + " мин. Дата старта " + formattedStartDate + "." +
                " Дата окончания " + formattedEndDate + ".";
    }
}
