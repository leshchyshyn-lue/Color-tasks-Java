package com.example.colortasks.controller;

import com.example.colortasks.converter.TaskConverter;
import com.example.colortasks.dto.RequestTaskDTO;
import com.example.colortasks.entity.Task;
import com.example.colortasks.exception.AlreadyExistsException;
import com.example.colortasks.exception.MustContainException;
import com.example.colortasks.exception.NotFoundException;
import com.example.colortasks.service.TaskService;
import com.example.colortasks.service.UserService;
import com.example.colortasks.util.TaskColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/sch")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final TaskConverter taskConverter;

    @Autowired
    public TaskController(TaskService taskService, UserService userService, TaskConverter taskConverter) {
        this.taskService = taskService;
        this.userService = userService;
        this.taskConverter = taskConverter;
    }

    @GetMapping("/tasks")
    public List<Task> findListItems() {
        return userService.findUser().getTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findTaskById(@PathVariable("id") int id) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findTaskById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Task> createNewTask(@RequestBody RequestTaskDTO requestTaskDTO) throws MustContainException, AlreadyExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.createNewTask(taskConverter.convertToTask(requestTaskDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@RequestBody Task task,
                                           @PathVariable("id") int id) throws NotFoundException, MustContainException, AlreadyExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTask(task, id));
    }

    @DeleteMapping("/{id}")
    public void deleteTaskById(@PathVariable("id") int id) throws NotFoundException {
        taskService.deleteTaskById(id);
    }

    @DeleteMapping("/clear")
    public void deleteAllTasksByUserId() {
        taskService.deleteAllTasksByUserId();
    }

    @DeleteMapping("/clear/{color}")
    public void deleteTasksByColor(@PathVariable("color") TaskColor color) {
        taskService.deleteAllTasksByColor(color);
    }

    @GetMapping("/by/{color}")
    public ResponseEntity<List<Task>> findAllTasksByColor(@PathVariable("color") TaskColor color) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findAllTasksByColor(color));
    }


}
