package com.uapp.similartrello.repository;

import com.uapp.similartrello.model.Task;

import java.util.Collection;
import java.util.List;

public interface TaskRepository {

    List<Task> get(Collection<Integer> ids);

    List<Task> getByGroups(Collection<Integer> ids);

    List<Task> getAll();

    void delete(Task task);

    void save(Task task);

    void updatePosition(List<Task> tasks);
}
