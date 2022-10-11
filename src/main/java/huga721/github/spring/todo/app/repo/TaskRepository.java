package huga721.github.spring.todo.app.repo;

import huga721.github.spring.todo.app.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// white list interface
public interface TaskRepository {
    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    Optional<Task> findById(Integer id);

    boolean existsById(Integer id);

    Task save(Task entity);

    List<Task> findByDone(@Param("state") boolean done);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);
}

