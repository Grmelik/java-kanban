package practicum.tasks;

public class Subtask extends Task{
    private int epicId; // Id родительского эпика

    public Subtask(String name, String description, int epicId, int id, TaskStatus status) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return '\n' + "ID=[" + id + "] Подзадача : " + name + " (" + description + "). Статус " + status +
                ". Родительский эпик = " + epicId;
    }
}
