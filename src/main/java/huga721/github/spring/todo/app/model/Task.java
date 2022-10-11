package huga721.github.spring.todo.app.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task extends TaskSuperClass {

    @Embedded
    private Audit audit = new Audit();
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;
    private LocalDateTime deadline;


    // Constructor only for Hibernate use, Hibernate is creating entity and then reading it from the db
    Task() {
    }


    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
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
    void setDescription(final String description) {
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

    public void updateFrom(final Task source) {
        setDescription(source.getDescription());
        setDone(source.isDone());
        deadline = source.deadline;
//        group = source.group;
    }
}
