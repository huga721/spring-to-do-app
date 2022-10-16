package huga721.github.spring.todo.app.model.DTO;

import huga721.github.spring.todo.app.model.entities.Task;
import huga721.github.spring.todo.app.model.entities.TaskGroup;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is how the entity should look for user, it's like projection to show how it should look
 * This is what he should input
 */
// DTO for TaskGroup
public class GroupReadModel {
    private String description;
    // Deadline from the latest task in group.
    private LocalDateTime deadline;
    private Set<GroupTaskReadModel> tasks;

    public GroupReadModel(TaskGroup source) {
        description = source.getDescription();

        source.getTasks().stream()
                .map(Task::getDeadline)
                .max(LocalDateTime::compareTo)
                .ifPresent(date -> deadline = date);

        tasks = source.getTasks().stream()
                .map(GroupTaskReadModel::new)
                .collect(Collectors.toSet());
    }

    public String getDescription() {
        return description;
    }

    void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    void setTasks(final Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }
}
