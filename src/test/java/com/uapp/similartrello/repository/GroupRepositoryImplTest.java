package com.uapp.similartrello.repository;

import com.uapp.similartrello.mapper.GroupMapper;
import com.uapp.similartrello.model.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
class GroupRepositoryImplTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private GroupRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        GroupMapper productMapper = new GroupMapper();
        repository = new GroupRepositoryImpl(namedParameterJdbcTemplate, productMapper);
    }

    @Test
    @Sql("group-data.sql")
    void get_WhenExist_ThenReturnActualResult() {
        List<Group> expectedResult = List.of(
                createGroup(1, "To Do", 1),
                createGroup(2, "June 23rd", 3)
        );
        List<Group> actualResult = repository.get(List.of(1, 2));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void get_WhenNotExist_ThenReturnEmptyList() {
        List<Group> expectedResult = Collections.emptyList();
        List<Group> actualResult = repository.get(List.of(1, 2));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @Sql("group-data.sql")
    void getAll_WhenExist_ThenReturnActualResult() {
        List<Group> expectedResult = List.of(
                createGroup(1, "To Do", 1),
                createGroup(2, "June 23rd", 3),
                createGroup(3, "Previous Meetings", 2)
        );
        List<Group> actualResult = repository.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getAll_WhenNotExist_ThenReturnEmptyList() {
        List<Group> expectedResult = Collections.emptyList();
        List<Group> actualResult = repository.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @Sql("group-data.sql")
    void delete_WhenDelete_ThenReturnActualResult() {
        List<Group> expectedResult = List.of(
                createGroup(1, "To Do", 1),
                createGroup(3, "Previous Meetings", 2)
        );
        repository.delete(createGroup(2, "June 23rd", 3));
        List<Group> actualResult = repository.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @Sql("group-data.sql")
    void save_WhenSave_ThenReturnActualResult() {
        Group group = createGroup(1, "To Do new 1", 1);
        List<Group> expectedResult = List.of(group);
        repository.save(group);
        List<Group> actualResult = repository.get(List.of(1));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void save_WhenSaveNew_ThenReturnWithId() {
        Group group = createGroup(null, "To Do", 1);
        repository.save(group);
        Integer actualResult = group.getId();

        assertNotNull(actualResult);
    }

    @Test
    @Sql("group-data.sql")
    void save_WhenNotSave_ThenReturnException() {
        Group group = createGroup(null, "To Do", 1);

        assertThrows(DuplicateKeyException.class, () -> repository.save(group));
    }

    @Test
    @Sql("group-data.sql")
    void updatePosition_WhenUpdate_ThenReturnActualResult() {
        List<Group> expectedResult = List.of(
                createGroup(1, "To Do", 4),
                createGroup(2, "June 23rd", 5),
                createGroup(3, "Previous Meetings", 6)
        );
        repository.updatePosition(expectedResult);
        List<Group> actualResult = repository.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @Sql("group-data.sql")
    void updatePosition_WhenPositionExist_ThenReturnException() {
        List<Group> groups = List.of(
                createGroup(1, "To Do", 3)
        );

        assertThrows(DuplicateKeyException.class, () -> repository.updatePosition(groups));
    }

    private Group createGroup(Integer id, String name, int position) {
        Group group = new Group();
        group.setId(id);
        group.setName(name);
        group.setPosition(position);

        return group;
    }
}