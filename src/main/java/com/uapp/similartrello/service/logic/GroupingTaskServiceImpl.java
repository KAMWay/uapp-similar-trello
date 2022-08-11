package com.uapp.similartrello.service.logic;

import com.uapp.similartrello.model.Group;
import com.uapp.similartrello.model.Task;
import com.uapp.similartrello.service.GroupService;
import com.uapp.similartrello.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GroupingTaskServiceImpl implements GroupingTaskService {

    private final GroupService groupService;
    private final TaskService taskService;

    public GroupingTaskServiceImpl(GroupService groupService, TaskService taskService) {
        this.groupService = groupService;
        this.taskService = taskService;
    }

    @Override
    public Map<Group, List<Task>> getGroupingTaskMap() {
        final List<Group> groups = groupService.getAll();
        final List<Task> tasks = taskService.getAll();

        return groups.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        group -> tasks.stream()
                                .filter(task -> task.getGroupId().equals(group.getId()))
                                .collect(Collectors.toCollection(ArrayList::new)),
                        (oldValue, newValue) -> oldValue)
                );
    }

    @Override
    public Map<Group, List<Task>> getSortedGroupingTaskMap(Map<Group, List<Task>> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(Group::getPosition)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> sortTaskList(e.getValue()),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new)
                );
    }

    private List<Task> sortTaskList(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getPosition))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
