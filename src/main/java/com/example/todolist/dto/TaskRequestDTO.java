package com.example.todolist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskRequestDTO {

    @NotBlank(message = "Заголовок задачи не может быть пустым")
    @Size(max = 200, message = "Заголовок не должен превышать 200 символов")
    private String title;

    @Size(max = 2000, message = "Описание не должно превышать 2000 символов")
    private String description;

    public TaskRequestDTO() {
    }

    public TaskRequestDTO(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
