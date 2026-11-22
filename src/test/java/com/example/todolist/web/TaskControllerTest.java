package com.example.todolist.web;

import com.example.todolist.dto.TaskRequestDTO;
import com.example.todolist.dto.TaskResponseDTO;
import com.example.todolist.exception.ResourceNotFoundException;
import com.example.todolist.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldReturn201AndTask_whenCreateValid() throws Exception {
        TaskResponseDTO response = new TaskResponseDTO(1L, "Тест", "Описание",
                false, LocalDateTime.now());
        when(taskService.createTask(any(TaskRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Тест","description":"Описание"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Тест"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void shouldReturn400_whenTitleIsBlank() throws Exception {
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"","description":"Описание"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Ошибка валидации входных данных"))
                .andExpect(jsonPath("$.details[0]").exists());
    }

    @Test
    void shouldReturn404_whenTaskNotFound() throws Exception {
        when(taskService.getTaskById(999L))
                .thenThrow(new ResourceNotFoundException("Задача с id=999 не найдена"));

        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Задача с id=999 не найдена"));
    }

    @Test
    void shouldReturn204_whenDeleteExistingTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }
}