package huga721.github.spring.todo.app.service;

import huga721.github.spring.todo.app.TaskConfigurationProperties;
import huga721.github.spring.todo.app.model.entities.Project;
import huga721.github.spring.todo.app.model.entities.Task;
import huga721.github.spring.todo.app.model.entities.TaskGroup;
import huga721.github.spring.todo.app.model.DTO.GroupReadModel;
import huga721.github.spring.todo.app.model.repo.ProjectRepository;
import huga721.github.spring.todo.app.model.repo.TaskGroupRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private TaskGroupRepository groupRepository;
    private TaskConfigurationProperties config;

    ProjectService(final ProjectRepository projectRepository, final TaskGroupRepository groupRepository,
                   final TaskConfigurationProperties config) {
        this.projectRepository = projectRepository;
        this.groupRepository = groupRepository;
        this.config = config;
    }
    // Method to read all
    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(final Project toSave) {
        return projectRepository.save(toSave);
    }


    public GroupReadModel createTaskGroup(int projectId, LocalDateTime deadline) {
        if (!config.getTemplate().isAllowMultipleTasks() &&
                groupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }

        TaskGroup taskGroup = projectRepository.findById(projectId)
                .map(project -> {
                    var result = new TaskGroup();
                    result.setDescription(project.getDescription());
                    result.setTasks(
                            project.getSteps().stream()
                                    .map(step -> {
                                        var task = new Task();
                                        task.setDescription(step.getDescription());
                                        task.setDeadline(deadline.plusDays(step.getDaysToDeadline()));
                                        return task;
                                    }).collect(Collectors.toSet()));
                    result.setProject(project);
                    return groupRepository.save(result);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(taskGroup);

    }
}
