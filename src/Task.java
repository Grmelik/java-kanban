import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status;

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.id = -1;
        this.status = status;
    }

    /*public Task(int id, TaskStatus status) {
        this.id = id;
        this.status = status;
    }*/

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        //return Objects.hash(name, description, id, status);
    }

    @Override
    public String toString() {
        String partOne = "ID=[" + id + "] ";
        String partTwo = String.valueOf(getClass()).substring(6);
        String partThree = ": " + name + " (" + description + ")" + ". Статус " + status;

        String result = switch (partTwo) {
            case "Task" -> "Задача ";
            case "Epic" -> "Эпик ";
            case "Subtask" -> "  ==> Подзадача ";
            default -> "";
        };

        result = partOne + result + partThree;
        return result;

        /*if ( )
        return getClass() + "{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';*/
    }
}
