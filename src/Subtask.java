public class Subtask extends Task{
    int epicId;
    int subtaskId;

    public Subtask(String name, String description, int epicId, TaskStatus status) {
        super(name, description, status);
        this.epicId = epicId;
        this.subtaskId = -1;
    }
}
