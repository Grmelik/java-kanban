public class Epic extends Task{
    int epicId;

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        this.epicId = -1;
    }

}
