package com.uapp.similartrello.service.logic;

import com.uapp.similartrello.model.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GroupPositionCalculationServiceTest {

    private List<Group> groups;
    private GroupPositionCalculationService service;

    @BeforeEach
    void setUp() {
        groups = List.of(
                createGroup(1, "To Do", 1),
                createGroup(2, "June 23rd", 3),
                createGroup(3, "Previous Meetings", 2)
        );
        service = new GroupPositionCalculationService();
    }

    static Stream<Arguments> arguments4UpdatePosition() {
        return Stream.of(
                Arguments.arguments(0, createGroup(1, "To Do", 2), 1),
                Arguments.arguments(3, createGroup(1, "To Do", 2), 2),
                Arguments.arguments(3, createGroup(1, "To Do", 0), 3),
                Arguments.arguments(3, createGroup(null, "To Do", 0), 4)
        );
    }

    @ParameterizedTest
    @MethodSource("arguments4UpdatePosition")
    void getUpdatePositionWhenExistShouldReturnActualResult(int listSize, Group group, int expectedResult) {
        int actualResult = service.getUpdatePosition(listSize, group);

        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> arguments4GetList4UpdatePositionIfDelete() {
        return Stream.of(
                Arguments.arguments(
                        createGroup(1, "To Do", 1),
                        List.of(
                                createGroup(3, "Previous Meetings", 1),
                                createGroup(2, "June 23rd", 2)
                        )
                ),
                Arguments.arguments(
                        createGroup(2, "June 23rd", 3),
                        Collections.emptyList()
                ),
                Arguments.arguments(
                        createGroup(3, "Previous Meetings", 2),
                        List.of(
                                createGroup(2, "June 23rd", 2)
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("arguments4GetList4UpdatePositionIfDelete")
    void getList4UpdatePositionIfDelete(Group group, List<Group> expectedResult) {
        List<Group> actualResult = service.getList4UpdatePositionIfDelete(groups, group);

        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> arguments4GetList4UpdatePositionIfSave() {
        return Stream.of(
                Arguments.arguments(
                        createGroup(1, "To Do", 1),
                        Collections.emptyList()
                ),
                Arguments.arguments(
                        createGroup(1, "To Do", 2),
                        List.of(
                                createGroup(3, "Previous Meetings", 1)
                        )
                ),
                Arguments.arguments(
                        createGroup(1, "To Do", 3),
                        List.of(
                                createGroup(3, "Previous Meetings", 1),
                                createGroup(2, "June 23rd", 2)
                        )
                ),
                Arguments.arguments(
                        createGroup(2, "June 23rd", 1),
                        List.of(
                                createGroup(3, "Previous Meetings", 3),
                                createGroup(1, "To Do", 2)
                        )
                ),
                Arguments.arguments(
                        createGroup(null, "Other", 1),
                        List.of(
                                createGroup(2, "June 23rd", 4),
                                createGroup(3, "Previous Meetings", 3),
                                createGroup(1, "To Do", 2)
                        )
                ),
                Arguments.arguments(
                        createGroup(null, "Other", 3),
                        List.of(
                                createGroup(2, "June 23rd", 4)
                        )
                ),
                Arguments.arguments(
                        createGroup(null, "Other", 4),
                        Collections.emptyList()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("arguments4GetList4UpdatePositionIfSave")
    void getList4UpdatePositionIfSave(Group group, List<Group> expectedResult) {
        List<Group> actualResult = service.getList4UpdatePositionIfSave(groups, group);

        assertEquals(expectedResult, actualResult);
    }

    private static Group createGroup(Integer id, String name, int position) {
        Group group = new Group();
        group.setId(id);
        group.setName(name);
        group.setPosition(position);

        return group;
    }
}