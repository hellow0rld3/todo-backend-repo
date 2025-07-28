package com.example.todobackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:3000") // Dla frontendu React
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // GET /api/tasks - pobierz wszystkie zadania
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // POST /api/tasks - utwórz nowe zadanie
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    // PUT /api/tasks/{id} - edytuj zadanie
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable UUID id, @RequestBody Task taskDetails) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(taskDetails.getTitle());
                    task.setDescription(taskDetails.getDescription());
                    task.setDone(taskDetails.getDone());
                    return ResponseEntity.ok(taskRepository.save(task));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/tasks/{id} - usuń zadanie
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable UUID id) {
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // PATCH /api/tasks/{id}/toggle - przełącz status done
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Task> toggleTask(@PathVariable UUID id) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setDone(!task.getDone());
                    return ResponseEntity.ok(taskRepository.save(task));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}