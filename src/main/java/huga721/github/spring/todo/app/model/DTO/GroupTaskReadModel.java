package huga721.github.spring.todo.app.model.DTO;

import huga721.github.spring.todo.app.model.entities.Task;
/**
 * This is how the entity should look for user, it's like projection to show how it should look
 * This is what he should input
 */
// Task read from within the group
public class GroupTaskReadModel {
    private String description;
    private boolean done;

    GroupTaskReadModel(Task source) {
        description = source.getDescription();
        done = source.isDone();
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
