package huga721.github.spring.todo.app.controller;

import huga721.github.spring.todo.app.model.entities.Task;
import huga721.github.spring.todo.app.model.repo.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping(path = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Exposing all tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.warn("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Task> readTaskById(@PathVariable int id) {
        if (repository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        logger.warn("Exposing task with id " + id);
        return ResponseEntity.ok(repository.findById(id).get());
    }

    @PutMapping("/tasks/{id}")
    ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id).ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    repository.save(task);
                });
        return ResponseEntity.noContent().build();
    }

    // To use transaction, 1. Annotation @Transactional, 2. public method
    @Transactional
    @PatchMapping("/tasks/{id}")
    public ResponseEntity<Task> toggleTask(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // If task with identification from param is true then set it to opposite, if true then false etc.
        repository.findById(id).ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();   // No content = 204 status code
    }

    @PostMapping("/tasks")
    ResponseEntity<Task> createNewTask(@RequestBody @Valid Task newTask) {
        // First way of post mapping
        Task result = repository.save(newTask);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
        // Second way
//        return new ResponseEntity<Task>(repository.save(newTask), HttpStatus.CREATED);
    }
}
