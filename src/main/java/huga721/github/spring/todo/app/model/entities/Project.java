package huga721.github.spring.todo.app.model.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @NotBlank(message = "Description can't be null")
    private String description;
    @OneToMany(mappedBy = "project")
    Set<TaskGroup> tasks;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    Set<ProjectSteps> steps;

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

    public Set<TaskGroup> getTasks() {
        return tasks;
    }

    void setTasks(final Set<TaskGroup> tasks) {
        this.tasks = tasks;
    }

    public Set<ProjectSteps> getSteps() {
        return steps;
    }

    void setSteps(final Set<ProjectSteps> steps) {
        this.steps = steps;
    }
}