package com.uapp.similartrello.controller;

import com.uapp.similartrello.exception.NotFoundException;
import com.uapp.similartrello.exception.ValidateException;
import com.uapp.similartrello.form.TaskForm;
import com.uapp.similartrello.model.Task;
import com.uapp.similartrello.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("task")
@Api("Tasks REST controller")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    @ApiOperation("Get all tasks")
    public List<Task> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get task by id")
    public Task get(@PathVariable Integer id) {
        return service.get(List.of(id)).stream().findFirst()
                .orElseThrow(() -> new NotFoundException("Not found task by id " + id));
    }

    @PostMapping
    @ApiOperation("Save task")
    public Task save(@ModelAttribute("form") @Valid TaskForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidateException(bindingResult.getAllErrors());
        }

        final Task task = getTask(form);
        service.save(task);

        return task;
    }

    @DeleteMapping
    @ApiOperation("Delete task")
    public void delete(@ModelAttribute("form") @Valid TaskForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidateException(bindingResult.getAllErrors());
        }

        service.delete(getTask(form));
    }

    private Task getTask(TaskForm form) {
        final Task task = new Task();
        task.setId(form.getId());
        task.setName(form.getName());
        task.setDescription(form.getDescription());
        task.setDateCreate(LocalDate.now());
        task.setPosition(form.getPosition());
        task.setGroupId(form.getGroupId());

        return task;
    }
}
