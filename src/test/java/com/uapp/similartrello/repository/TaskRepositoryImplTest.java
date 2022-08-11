package com.uapp.similartrello.repository;

import com.uapp.similartrello.mapper.TaskMapper;
import com.uapp.similartrello.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
class TaskRepositoryImplTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private TaskRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        TaskMapper taskMapper = new TaskMapper();
        repository = new TaskRepositoryImpl(namedParameterJdbcTemplate, taskMapper);
    }

    @Test
    @Sql("task-data.sql")
    void get_WhenExist_ThenReturnActualResult() {
        List<Task> expectedResult = List.of(
                createTask(1, "Task 1.1", "Something to do 1", LocalDate.parse("2012-06-30", formatter), 1, 1),
                createTask(2, "Task 1.2", "Something to do 2", LocalDate.parse("2012-05-10", formatter), 2, 1)
        );

        List<Task> actualResult = repository.get(List.of(1, 2));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void get_WhenNotExist_ThenReturnEmptyList() {
        List<Task> expectedResult = Collections.emptyList();
        List<Task> actualResult = repository.get(List.of(1, 2));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @Sql("task-data.sql")
    void getByGroups_WhenExist_ThenReturnActualResult() {
        List<Task> expectedResult = List.of(
                createTask(1, "Task 1.1", "Something to do 1", LocalDate.parse("2012-06-30", formatter), 1, 1),
                createTask(2, "Task 1.2", "Something to do 2", LocalDate.parse("2012-05-10", formatter), 2, 1)
        );

        List<Task> actualResult = repository.getByGroups(List.of(1));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getByGroups_WhenNotExist_ThenReturnEmptyList() {
        List<Task> expectedResult = Collections.emptyList();
        List<Task> actualResult = repository.getByGroups(List.of(1, 2));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @Sql("task-data.sql")
    void getAll_WhenExist_ThenReturnActualResult() {
        List<Task> expectedResult = List.of(
                createTask(1, "Task 1.1", "Something to do 1", LocalDate.parse("2012-06-30", formatter), 1, 1),
                createTask(2, "Task 1.2", "Something to do 2", LocalDate.parse("2012-05-10", formatter), 2, 1)
        );
        List<Task> actualResult = repository.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getAll_WhenNotExist_ThenReturnEmptyList() {
        List<Task> expectedResult = Collections.emptyList();
        List<Task> actualResult = repository.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @Sql("task-data.sql")
    void delete_WhenDelete_ThenReturnActualResult() {
        List<Task> expectedResult = List.of(
                createTask(2, "Task 1.2", "Something to do 2", LocalDate.parse("2012-05-10", formatter), 2, 1)
        );
        repository.delete(createTask(1, "Task 1.1", "Something to do 1", LocalDate.parse("2012-06-30", formatter), 1, 1));
        List<Task> actualResult = repository.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @Sql("task-data.sql")
    void save_WhenSave_ThenReturnActualResult() {
        Task task = createTask(1, "Task 1.1.0", "Something to do 1", LocalDate.parse("2012-06-30", formatter), 1, 2);
        List<Task> expectedResult = List.of(task);
        repository.save(task);
        List<Task> actualResult = repository.get(List.of(1));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @Sql("group-data.sql")
    void save_WhenSaveNew_ThenReturnWithId() {
        Task task = createTask(null, "Task 1.1", "Something to do 1", LocalDate.parse("2012-06-30", formatter), 1, 1);
        repository.save(task);
        Integer actualResult = task.getId();

        assertNotNull(actualResult);
    }

    @Test
    @Sql("task-data.sql")
    void save_WhenNotSave_ThenReturnException() {
        Task task = createTask(null, "Task 1.1", "Something to do 1", LocalDate.parse("2012-06-30", formatter), 1, 1);

        assertThrows(DuplicateKeyException.class, () -> repository.save(task));
    }

    @Test
    @Sql("task-data.sql")
    void updatePosition_WhenUpdate_ThenReturnActualResult() {
        List<Task> expectedResult = List.of(
                createTask(1, "Task 1.1", "Something to do 1", LocalDate.parse("2012-06-30", formatter), 3, 1),
                createTask(2, "Task 1.2", "Something to do 2", LocalDate.parse("2012-05-10", formatter), 4, 1)
        );
        repository.updatePosition(expectedResult);
        List<Task> actualResult = repository.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @Sql("task-data.sql")
    void updatePosition_WhenPositionExist_ThenReturnException() {
        List<Task> tasks = List.of(
                createTask(1, "Task 1.1", "Something to do 1", LocalDate.parse("2012-06-30", formatter), 2, 1),
                createTask(2, "Task 1.2", "Something to do 2", LocalDate.parse("2012-05-10", formatter), 3, 1)
        );

        assertThrows(DuplicateKeyException.class, () -> repository.updatePosition(tasks));
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
}