package huga721.github.spring.todo.app.logic;

import huga721.github.spring.todo.app.model.Task;
import huga721.github.spring.todo.app.repo.TaskGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class TempService {
    @Autowired
    List<String> temp(TaskGroupRepository repository) {
        return repository.findAll().stream()
                .flatMap(taskGroup ->   taskGroup.getTasks().stream())
                .map(Task::getDescription)
                .collect(Collectors.toList());
    }
}
