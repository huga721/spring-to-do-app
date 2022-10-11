package huga721.github.spring.todo.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
class TaskSuperClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Task's description must not be empty")
    private String description;

    private boolean done;


    public int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(final String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    void setDone(final boolean done) {
        this.done = done;
    }
}
