package practicum.tasks;

public class Subtask extends Task {
    private int epicId; // Id родительского эпика

    public Subtask(String name, String description, TaskStatus status) {
        super(name, description, status);
        this.epicId = -1;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    /*@Override
    public String toString() {
        return "ID=[" + id + "]: " + name + " (" + description + "). Статус " + status + ".";
    }*/
}
