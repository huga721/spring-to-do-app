package huga721.github.spring.todo.app.service;

import huga721.github.spring.todo.app.TaskConfigurationProperties;
import huga721.github.spring.todo.app.model.DTO.GroupReadModel;
import huga721.github.spring.todo.app.model.entities.Project;
import huga721.github.spring.todo.app.model.entities.ProjectSteps;
import huga721.github.spring.todo.app.model.entities.TaskGroup;
import huga721.github.spring.todo.app.model.repo.ProjectRepository;
import huga721.github.spring.todo.app.model.repo.TaskGroupRepository;
import huga721.github.spring.todo.app.model.repo.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and undone group exist")
    void createGroup_noMultipleGroupConfig_And_openGroups_throwsIllegalStateException() {
        // 1. given (preparing data for test)
        // using mockito to create reference to classes so mockito gonna override methods etc.
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        // AND
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        // System under test
        var toTest = new ProjectService(null, mockGroupRepository, mockConfig);

        // when
        var exception = catchThrowable(() -> toTest.createTaskGroup(0, LocalDateTime.now()));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("one undone group");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration true and no projects for a given id")
    void createGroup_noMultipleGroupConfig_And_noProjects_throwsIllegalArgumentException() {
        // 1. given (preparing data for test)
        // using mockito to create reference to classes so mockito gonna override methods etc.
        var mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt())).thenReturn(Optional.empty());
        // AND
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // System under test
        var toTest = new ProjectService(mockProjectRepository, null, mockConfig);

        // when
        var exception = catchThrowable(() -> toTest.createTaskGroup(0, LocalDateTime.now()));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just one group and no groups and projects for a given id")
    void createGroup_ConfigurationTrue_And_noProjects_throwsIllegalArgumentException() {
        // 1. given (preparing data for test)
        // using mockito to create reference to classes so mockito gonna override methods etc.
        ProjectRepository mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt())).thenReturn(Optional.empty());
        // AND
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
        // AND
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // System under test
        var toTest = new ProjectService(mockProjectRepository, mockGroupRepository, mockConfig);

        // when
        var exception = catchThrowable(() -> toTest.createTaskGroup(0, LocalDateTime.now()));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should crete new group from project")
    void createGroup_ConfigurationTrue_Id_Ok_ExistingProject_CreatesAndSavesGroup() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        // given
        Project mockProjectDescription = mockProjectDescription("test", Set.of(-1,-2));
        ProjectRepository mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt()))
                .thenReturn(Optional.of(mockProjectDescription));
        // AND
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        int countBeforeCall = inMemoryGroupRepo.count();
        // AND
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // class to be tested
        ProjectService toTest = new ProjectService(mockProjectRepository, inMemoryGroupRepo, mockConfig);

        // when
        GroupReadModel result = toTest.createTaskGroup(1, today);
        assertThat(result.getDescription()).isEqualTo("test");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("Temp desc"));
        assertThat(result).hasFieldOrPropertyWithValue("description", "test");

        // then
        assertThat(countBeforeCall)
                .isEqualTo(inMemoryGroupRepo.count() - 1);
    }

    // Mocking Project entity to get description
    private Project mockProjectDescription(String projectDescription, Set<Integer> daysToDeadline) {
        Set<ProjectSteps> pSteps = daysToDeadline.stream()
                .map(days -> {
                    ProjectSteps steps = mock(ProjectSteps.class);
                    when(steps.getDescription()).thenReturn("Temp desc");
                    when(steps.getDaysToDeadline()).thenReturn(days);
                    return steps;
                }).collect(Collectors.toSet());
        Project result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(pSteps);
        return result;
    }
    // Mockito
    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        TaskGroupRepository mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }
    // Mockito
    private TaskConfigurationProperties configurationReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    // inner class
    static class InMemoryGroupRepository implements TaskGroupRepository {

        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(final Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(final TaskGroup entity) {
            if (entity.getId() == 0) {
                try {
                    Field field = TaskGroup.class.getSuperclass().getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(final Integer projectId) {
            return map.values().stream()
                    .filter(taskGroup -> !taskGroup.isDone())
                    .anyMatch(taskGroup -> taskGroup.getProject() != null && taskGroup.getId() == projectId);
        }
    }
}
