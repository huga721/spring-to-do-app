package huga721.github.spring.todo.app.model.entities;

import huga721.github.spring.todo.app.model.entities.Project;
import huga721.github.spring.todo.app.model.entities.Task;
import huga721.github.spring.todo.app.model.entities.TaskSuperClass;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "task_groups")
public class TaskGroup extends TaskSuperClass {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Task> tasks;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    // Constructor only for Hibernate use, Hibernate is creating entity and then reading it from the db
    public TaskGroup() {
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void setId(final int id) {
        super.setId(id);
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public void setDescription(final String description) {
        super.setDescription(description);
    }

    @Override
    public boolean isDone() {
        return super.isDone();
    }

    @Override
    public void setDone(final boolean done) {
        super.setDone(done);
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }
}
