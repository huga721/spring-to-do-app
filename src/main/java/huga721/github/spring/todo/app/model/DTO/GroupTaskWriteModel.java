package huga721.github.spring.todo.app.model.DTO;

import huga721.github.spring.todo.app.model.entities.Task;

import java.time.LocalDateTime;
/**
 * This is how the entity should look for user, it's like projection to show how it should look
 * This is what he should input
 */
// Task DTO(Data transfer object)
public class GroupTaskWriteModel {
    private String description;
    private LocalDateTime deadline;

    public String getDescription() {
        return description;
    }

    void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getDaysToDeadline() {
        return deadline;
    }

    void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Task toTask() {
        Task task = new Task();
        task.setDescription(description);
        task.setDeadline(deadline);
        return task;
    }
}
