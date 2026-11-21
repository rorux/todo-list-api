package com.example.todolist.repository;

import com.example.todolist.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Поиск задач по статусу выполнения.
     * Spring Data JPA автоматически сгенерирует запрос по имени метода.
     */
    List<Task> findByCompleted(boolean completed);
}
