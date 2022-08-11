package com.uapp.similartrello.service.logic;

import com.uapp.similartrello.model.Group;
import com.uapp.similartrello.model.Task;

import java.util.List;
import java.util.Map;

public interface GroupingTaskService {

    Map<Group, List<Task>> getGroupingTaskMap();

    Map<Group, List<Task>> getSortedGroupingTaskMap(Map<Group, List<Task>> groups);
}
