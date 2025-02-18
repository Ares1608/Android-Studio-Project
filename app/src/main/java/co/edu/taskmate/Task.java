package co.edu.taskmate;



public class Task {
    private int id;
    private String title;
    private String description;
    private String date;
    private int status;

    public Task(int id, String title, String description, String date, int status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public Task(String title, String description, String date, int status) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public int getStatus() { return status; }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
    public void setStatus(int status) { this.status = status; }
}
