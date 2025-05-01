package careplan.model;

public class Task {
    private String title;
    private boolean done;

    public Task(String title) {
        this.title = title;
        this.done = false;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
}

