package com.uapp.similartrello;

import com.uapp.similartrello.model.Group;
import com.uapp.similartrello.model.Task;
import com.uapp.similartrello.service.logic.GroupingTaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class SimilarTrelloApplicationTests {

    @Autowired
    private GroupingTaskService service;

    @Test
    @Transactional
    @Sql("grouping-map-data.sql")
    void getGroupsMap() {
        Group group1 = createGroup(1, "To Do", 1);
        Group group2 = createGroup(2, "June 23rd", 3);
        Group group3 = createGroup(3, "Previous Meetings", 2);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Task> tasks1 = List.of(
                createTask(1, "Task 1.1", "Something to do 1", LocalDate.parse("2012-06-30", formatter), 1, 1),
                createTask(3, "Task 1.2", "Something to do 2", LocalDate.parse("2012-05-10", formatter), 2, 1),
                createTask(2, "Task 1.3", "Something to do 3", LocalDate.parse("2012-05-30", formatter), 3, 1)
        );

        List<Task> tasks2 = List.of(
                createTask(4, "Task 2.1", "Some meeting 1", LocalDate.parse("2012-06-01", formatter), 1, 2),
                createTask(5, "Task 2.2", "Some meeting 2", LocalDate.parse("2012-06-02", formatter), 2, 2),
                createTask(6, "Task 2.3", "Some meeting 3", LocalDate.parse("2012-06-03", formatter), 3, 2),
                createTask(7, "Task 2.4", "Some meeting 4", LocalDate.parse("2012-07-20", formatter), 4, 2)
        );

        List<Task> tasks3 = List.of(
                createTask(8, "Task 3.1", "Some meeting 5", LocalDate.parse("2012-08-10", formatter), 1, 3)
        );

        Map<Group, List<Task>> expectedResult = new LinkedHashMap<>();
        expectedResult.put(group1, tasks1);
        expectedResult.put(group2, tasks2);
        expectedResult.put(group3, tasks3);

        Map<Group, List<Task>> result = service.getGroupingTaskMap();

        Map<Group, List<Task>> actualResult = service.getSortedGroupingTaskMap(result);

        assertEquals(expectedResult, actualResult);
    }

    private Task createTask(
            Integer id,
            String name,
            String description,
            LocalDate dateCreate,
            int position,
            Integer groupId) {
        Task task = new Task();
        task.setId(id);
        task.setName(name);
        task.setDescription(description);
        task.setDateCreate(dateCreate);
        task.setPosition(position);
        task.setGroupId(groupId);

        return task;
    }

    private Group createGroup(Integer id, String name, int position) {
        Group group = new Group();
        group.setId(id);
        group.setName(name);
        group.setPosition(position);

        return group;
    }

}
