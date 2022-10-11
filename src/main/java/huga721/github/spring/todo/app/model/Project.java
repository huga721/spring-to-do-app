package huga721.github.spring.todo.app.model;

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
    Set<TaskGroup> setTaskGroup;
    @OneToMany(mappedBy = "project")
    Set<ProjectSteps> setProjectSteps;

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

    public Set<TaskGroup> getSetTaskGroup() {
        return setTaskGroup;
    }

    void setSetTaskGroup(final Set<TaskGroup> setTaskGroup) {
        this.setTaskGroup = setTaskGroup;
    }

    public Set<ProjectSteps> getSetProjectSteps() {
        return setProjectSteps;
    }

    void setSetProjectSteps(final Set<ProjectSteps> setProjectSteps) {
        this.setProjectSteps = setProjectSteps;
    }
}