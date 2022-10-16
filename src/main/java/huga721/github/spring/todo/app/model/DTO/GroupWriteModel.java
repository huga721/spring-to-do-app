package huga721.github.spring.todo.app.model.DTO;

import huga721.github.spring.todo.app.model.entities.TaskGroup;

import java.util.Set;
import java.util.stream.Collectors;
/**
 * This is how the entity should look for user, it's like projection to show how it should look
 * This is what he should input
 */
public class GroupWriteModel {
    private String description; // description of whole group
    private Set<GroupTaskWriteModel> tasks;

    public String getDescription() {
        return description;
    }

    void setDescription(final String description) {
        this.description = description;
    }

    public Set<GroupTaskWriteModel> getTasks() {
        return tasks;
    }

    void setTasks(final Set<GroupTaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroup toGroup() {
        var result = new TaskGroup();
        result.setDescription(description);
        result.setTasks(
                tasks.stream()
                        .map(GroupTaskWriteModel::toTask)
                        .collect(Collectors.toSet()));
        return result;
    }
}
