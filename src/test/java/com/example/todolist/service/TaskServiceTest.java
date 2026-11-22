package com.example.todolist.service;

import com.example.todolist.domain.Task;
import com.example.todolist.dto.TaskRequestDTO;
import com.example.todolist.dto.TaskResponseDTO;
import com.example.todolist.exception.ResourceNotFoundException;
import com.example.todolist.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldCreateTask_whenValidRequest() {
        // Given
        TaskRequestDTO dto = new TaskRequestDTO("Купить молоко", "2 литра");
        Task savedTask = new Task("Купить молоко", "2 литра");
        savedTask.setId(1L);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // When
        TaskResponseDTO result = taskService.createTask(dto);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Купить молоко");
        assertThat(result.isCompleted()).isFalse();
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void shouldThrowResourceNotFoundException_whenTaskNotFound() {
        // Given
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.getTaskById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("id=999");
    }

    @Test
    void shouldUpdateTaskFields_whenTaskExists() {
        // Given
        Task existing = new Task("Старый", "Старое описание");
        existing.setId(1L);
        TaskRequestDTO dto = new TaskRequestDTO("Новый", "Новое описание");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));

        // When
        TaskResponseDTO result = taskService.updateTask(1L, dto);

        // Then
        assertThat(result.getTitle()).isEqualTo("Новый");
        assertThat(result.getDescription()).isEqualTo("Новое описание");
        // save() не вызывается явно — dirty checking
        verify(taskRepository, never()).save(any());
    }
}