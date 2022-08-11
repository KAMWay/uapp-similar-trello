package com.uapp.similartrello.service;

import com.uapp.similartrello.exception.NotFoundException;
import com.uapp.similartrello.model.Task;
import com.uapp.similartrello.repository.TaskRepository;
import com.uapp.similartrello.service.logic.PositionCalculationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final PositionCalculationService<Task> calculationService;

    public TaskServiceImpl(TaskRepository repository, PositionCalculationService<Task> calculationService) {
        this.repository = repository;
        this.calculationService = calculationService;
    }

    @Override
    public List<Task> get(Collection<Integer> ids) {
        return repository.get(ids);
    }

    @Override
    public List<Task> getAll() {
        return repository.getAll();
    }

    @Override
    @Transactional
    public void delete(Task task) {
        final List<Task> tasks = repository.getByGroups(List.of(task.getGroupId()));
        repository.delete(task);

        final List<Task> updateTasks = calculationService.getList4UpdatePositionIfDelete(tasks, task);
        updateTasksPosition(updateTasks);
    }

    @Override
    @Transactional
    public void save(Task task) {
        if (task.getId() == null) {
            saveToCurrentGroup(task);
            return;
        }

        final Task oldTask =  repository.get(List.of(task.getId())).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found task by id " + task.getId()));

        if (Objects.equals(oldTask.getGroupId(), task.getGroupId())) {
            saveToCurrentGroup(task);
        } else {
            saveToOtherGroup(task, oldTask);
        }
    }

    private void saveToCurrentGroup(Task task) {
        final List<Task> tasks = repository.getByGroups(List.of(task.getGroupId()));

        task.setPosition(calculationService.getUpdatePosition(tasks.size(), task));

        final LinkedList<Task> updateTasks = new LinkedList<>(calculationService.getList4UpdatePositionIfSave(tasks, task));
        if (task.getId() == null || !isExistById(tasks, task)) {
            updateTasksPosition(updateTasks);
        } else {
            updateTasksPosition(updateTasks, task);
        }

        repository.save(task);
    }

    private void saveToOtherGroup(Task newTask, Task oldTask) {
        final List<Task> tasks = repository.getByGroups(List.of(oldTask.getGroupId()));
        final LinkedList<Task> oldTasks = new LinkedList<>(calculationService.getList4UpdatePositionIfDelete(tasks, oldTask));
        updateTasksPosition(oldTasks, oldTask);

        final List<Task> newTasks = repository.getByGroups(List.of(newTask.getGroupId()));
        if (newTask.getPosition() > newTasks.size() + 1 || newTask.getPosition() == 0) {
            newTask.setPosition(newTasks.size() + 1);
        }
        final LinkedList<Task> updateTasks = new LinkedList<>(calculationService.getList4UpdatePositionIfSave(newTasks, newTask));
        updateTasksPosition(updateTasks);
        repository.save(newTask);
    }

    private boolean isExistById(List<Task> tasks, Task task) {
        return tasks.stream()
                .anyMatch(e -> Objects.equals(e.getId(), task.getId()));
    }

    private void updateTasksPosition(List<Task> updateTasks, Task tmpTask) {
        final int tmp = tmpTask.getPosition();
        tmpTask.setPosition(0);
        updateTasks.add(0, tmpTask);
        updateTasksPosition(updateTasks);
        tmpTask.setPosition(tmp);
    }

    private void updateTasksPosition(List<Task> tasks) {
        if (!tasks.isEmpty()) {
            repository.updatePosition(tasks);
        }
    }
}
