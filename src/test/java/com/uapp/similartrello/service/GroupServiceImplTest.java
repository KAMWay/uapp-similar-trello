package com.uapp.similartrello.service;

import com.uapp.similartrello.model.Group;
import com.uapp.similartrello.repository.GroupRepository;
import com.uapp.similartrello.service.logic.PositionCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    private static final int GROUP_ID = 1;

    @Mock
    private GroupRepository repository;

    @Mock
    private PositionCalculationService<Group> calculationService;

    private GroupServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new GroupServiceImpl(repository, calculationService);
    }

    @Test
    void get_WhenExist_ThenReturnActualResult() {
        List<Group> expectedResult = List.of(createGroup());

        when(repository.get(List.of(GROUP_ID))).thenReturn(List.of(createGroup()));

        List<Group> actualResult = service.get(List.of(GROUP_ID));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void get_WhenNotExist_ThenReturnEmptyList() {
        List<Group> expectedResult = emptyList();

        when(repository.get(List.of(GROUP_ID))).thenReturn(emptyList());

        List<Group> actualResult = service.get(List.of(GROUP_ID));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getAll_WhenExist_ThenReturnActualResult() {
        List<Group> expectedResult = List.of(createGroup());

        when(repository.getAll()).thenReturn(List.of(createGroup()));

        List<Group> actualResult = service.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getAll_WhenNotExist_ThenReturnEmptyList() {
        List<Group> expectedResult = emptyList();

        when(repository.getAll()).thenReturn(emptyList());

        List<Group> actualResult = service.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void delete_MustVerify() {
        GroupServiceImpl groupService = mock(GroupServiceImpl.class);
        doNothing().when(groupService).delete(isA(Group.class));

        Group group = new Group();
        groupService.delete(group);

        verify(groupService).delete(group);
    }

    @Test
    void delete_MustAssert() {
        doNothing().when(repository).delete(isA(Group.class));
        when(calculationService.getList4UpdatePositionIfDelete(any(), isA(Group.class))).thenReturn(emptyList());

        assertAll(() -> service.delete(createGroup()));
    }

    @Test
    void save_MustVerify() {
        GroupServiceImpl groupService = mock(GroupServiceImpl.class);
        doNothing().when(groupService).save(isA(Group.class));

        Group group = new Group();
        groupService.save(group);

        verify(groupService).save(group);
    }

    static Stream<Arguments> arguments4save() {
        Group group1 = createGroup();
        Group group2 = createGroup();
        group2.setPosition(4);

        return Stream.of(
                Arguments.arguments(new Group()),
                Arguments.arguments(group1),
                Arguments.arguments(group2)
        );
    }

    @ParameterizedTest
    @MethodSource("arguments4save")
    void save_WhenExistTask_ThenAssert(Group group) {
        GroupServiceImpl groupService = mock(GroupServiceImpl.class);
        doNothing().when(groupService).save(isA(Group.class));

        groupService.save(group);
        verify(groupService).save(group);

        assertAll(() -> service.save(group));
    }

    private static Group createGroup() {
        Group group = new Group();
        group.setId(1);
        group.setName("To Do");
        group.setPosition(1);

        return group;
    }
}