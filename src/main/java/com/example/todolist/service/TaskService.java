package com.example.todolist.service;

import com.example.todolist.domain.Task;
import com.example.todolist.dto.TaskRequestDTO;
import com.example.todolist.dto.TaskResponseDTO;
import com.example.todolist.exception.ResourceNotFoundException;
import com.example.todolist.mapper.TaskMapper;
import com.example.todolist.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO dto) {
        Task task = new Task(dto.getTitle(), dto.getDescription());
        Task saved = taskRepository.save(task);
        return taskMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(taskMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO getTaskById(Long id) {
        Task task = findTaskOrThrow(id);
        return taskMapper.toResponse(task);
    }

    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO dto) {
        Task task = findTaskOrThrow(id);
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        return taskMapper.toResponse(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = findTaskOrThrow(id);
        taskRepository.delete(task);
    }

    /**
     * Вспомогательный метод: находит задачу или бросает ResourceNotFoundException.
     * Убирает дублирование в getTaskById / updateTask / deleteTask.
     */
    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Задача с id=" + id + " не найдена"));
    }
}
