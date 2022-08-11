package com.uapp.similartrello.service.logic;

import com.uapp.similartrello.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TaskPositionCalculationServiceTest {

    private List<Task> groups;
    private TaskPositionCalculationService service;

    @BeforeEach
    void setUp() {
        groups = List.of(
                createTask(1, 1),
                createTask(2, 3),
                createTask(3, 2)
        );
        service = new TaskPositionCalculationService();
    }

    static Stream<Arguments> arguments4Update() {
        return Stream.of(
                Arguments.arguments(0, createTask(1, 2), 1),
                Arguments.arguments(3, createTask(1, 2), 2),
                Arguments.arguments(3, createTask(1, 0), 3),
                Arguments.arguments(3, createTask(null, 0), 4)
        );
    }

    @ParameterizedTest
    @MethodSource("arguments4Update")
    void getList_WhenUpdate_ThenReturnActualResult(int listSize, Task task, int expectedResult) {
        int actualResult = service.getUpdatePosition(listSize, task);

        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> arguments4Delete() {
        return Stream.of(
                Arguments.arguments(
                        createTask(1, 1),
                        List.of(
                                createTask(3, 1),
                                createTask(2, 2)
                        )
                ),
                Arguments.arguments(
                        createTask(2, 3),
                        Collections.emptyList()
                ),
                Arguments.arguments(
                        createTask(3, 2),
                        List.of(createTask(2, 2))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("arguments4Delete")
    void getList_WhenDelete_ThenReturnActualResult(Task task, List<Task> expectedResult) {
        List<Task> actualResult = service.getList4UpdatePositionIfDelete(groups, task);

        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> arguments4Save() {
        return Stream.of(
                Arguments.arguments(
                        createTask(1, 1),
                        Collections.emptyList()
                ),
                Arguments.arguments(
                        createTask(1, 2),
                        List.of(createTask(3, 1))
                ),
                Arguments.arguments(
                        createTask(1, 3),
                        List.of(
                                createTask(3, 1),
                                createTask(2, 2)
                        )
                ),
                Arguments.arguments(
                        createTask(2, 1),
                        List.of(
                                createTask(3, 3),
                                createTask(1, 2)
                        )
                ),
                Arguments.arguments(
                        createTask(null, 1),
                        List.of(
                                createTask(2, 4),
                                createTask(3, 3),
                                createTask(1, 2)
                        )
                ),
                Arguments.arguments(
                        createTask(null, 3),
                        List.of(createTask(2, 4))
                ),
                Arguments.arguments(
                        createTask(null, 4),
                        Collections.emptyList()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("arguments4Save")
    void getList_WhenSave_ThenReturnActualResult(Task task, List<Task> expectedResult) {
        List<Task> actualResult = service.getList4UpdatePositionIfSave(groups, task);

        assertEquals(expectedResult, actualResult);
    }

    private static Task createTask(Integer id, int position) {
        Task task = new Task();
        task.setId(id);
        task.setPosition(position);

        return task;
    }
}