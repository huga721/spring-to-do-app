package huga721.github.spring.todo.app.service;

import huga721.github.spring.todo.app.model.entities.TaskGroup;
import huga721.github.spring.todo.app.model.repo.TaskGroupRepository;
import huga721.github.spring.todo.app.model.repo.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when existsByDoneIsFalseAndGroup_Id method is true")
    void toggleGroup_existsByDoneIsFalseAndGroup_Id_isTrue_throws_IllegalStateException() {
        // 1. Given. Preparing data for testing
        TaskRepository taskRepository = mockTaskRepository(true);

        // Class to test
        TaskGroupService taskGroupService = new TaskGroupService(null, taskRepository);
        // Test if it's going to throw exception
        Throwable exception = catchThrowable(() -> taskGroupService.toggleGroup(2));

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("undone tasks");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when findById return empty Optional")
    void toggleGroup_existsByDoneIsFalseAndGroup_Id_IsFalse_But_Int_InFindById_IsEmpty(){
        // given
        TaskRepository taskRepo = mockTaskRepository(false);
        // AND
        TaskGroupRepository taskGroupRepo = mock(TaskGroupRepository.class);
        when(taskGroupRepo.findById(anyInt())).thenReturn(Optional.empty());

        // Testing method
        TaskGroupService taskGroupService = new TaskGroupService(taskGroupRepo, taskRepo);

        Throwable exception = catchThrowable(() -> taskGroupService.toggleGroup(3));

        // Then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should change status of the group")
    void toggleGroup_success_done() {
        // Give
        TaskRepository taskRepo = mockTaskRepository(false);
        // AND
        TaskGroup group = new TaskGroup();
        boolean beforeToggle = group.isDone();
        // AND
        TaskGroupRepository taskGroupRepo = mock(TaskGroupRepository.class);
        when(taskGroupRepo.findById(anyInt())).thenReturn(Optional.of(group));
        // Creating object of class that  we want to test
        TaskGroupService toTest = new TaskGroupService(taskGroupRepo, taskRepo);

        toTest.toggleGroup(0);
        // Then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);
    }

    private TaskRepository mockTaskRepository(final boolean expected) {
        TaskRepository repo = mock(TaskRepository.class);
        when(repo.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(expected);
        return repo;
    }
}