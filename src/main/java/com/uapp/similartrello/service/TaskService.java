package com.uapp.similartrello.service;

import com.uapp.similartrello.model.Task;

import java.util.Collection;
import java.util.List;

public interface TaskService {

    List<Task> get(Collection<Integer> ids);

    List<Task> getAll();

    void delete(Task task);

    void save(Task task);
}
