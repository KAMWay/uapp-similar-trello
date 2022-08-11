package com.uapp.similartrello.service;

import com.uapp.similartrello.model.Task;
import com.uapp.similartrello.repository.TaskRepository;
import com.uapp.similartrello.service.logic.PositionCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    private static final int TASK_ID = 1;

    @Mock
    private TaskRepository repository;
    @Mock
    private PositionCalculationService<Task> calculationService;

    private TaskServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TaskServiceImpl(repository, calculationService);
    }

    @Test
    void get_WhenExist_ThenReturnActualResult() {
        List<Task> expectedResult = List.of(createTask());

        when(repository.get(List.of(TASK_ID))).thenReturn(List.of(createTask()));

        List<Task> actualResult = service.get(List.of(TASK_ID));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void get_WhenNotExist_ThenReturnEmptyList() {
        List<Task> expectedResult = emptyList();

        when(repository.get(List.of(TASK_ID))).thenReturn(emptyList());

        List<Task> actualResult = service.get(List.of(TASK_ID));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getAll_WhenExist_ThenReturnActualResult() {
        List<Task> expectedResult = List.of(createTask());

        when(repository.getAll()).thenReturn(List.of(createTask()));

        List<Task> actualResult = service.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getAll_WhenNotExist_ThenReturnEmptyList() {
        List<Task> expectedResult = emptyList();

        when(repository.getAll()).thenReturn(emptyList());

        List<Task> actualResult = service.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void delete_MustVerify() {
        TaskServiceImpl taskService = mock(TaskServiceImpl.class);
        doNothing().when(taskService).delete(isA(Task.class));

        Task task = new Task();
        taskService.delete(task);

        verify(taskService).delete(task);
    }

    @Test
    void delete_MustAssert() {
        when(repository.getByGroups(any())).thenReturn(emptyList());
        when(calculationService.getList4UpdatePositionIfDelete(any(), isA(Task.class))).thenReturn(emptyList());
        doNothing().when(repository).delete(isA(Task.class));

        assertAll(() -> service.delete(createTask()));
    }

    @Test
    void save_WhenNewTask_ThenMustAssert() {
        TaskServiceImpl taskService = mock(TaskServiceImpl.class);
        doNothing().when(taskService).save(isA(Task.class));

        Task task = createTask();
        task.setId(null);
        taskService.save(task);

        verify(taskService).save(task);

        when(repository.getByGroups(any())).thenReturn(emptyList());

        assertAll(() -> service.save(task));
    }

    static Stream<Arguments> arguments4save() {
        Task task1 = createTask();
        Task task2 = createTask();
        task2.setGroupId(2);
        Task task3 = createTask();
        task3.setGroupId(2);
        task3.setPosition(4);
        return Stream.of(
                Arguments.arguments(task1, List.of(task1)),
                Arguments.arguments(task2, List.of(task1)),
                Arguments.arguments(task3, List.of(task1))
        );
    }

    @ParameterizedTest
    @MethodSource("arguments4save")
    void save_WhenExistTask_ThenAssert(Task task, List<Task> tasks) {
        TaskServiceImpl taskService = mock(TaskServiceImpl.class);

        doNothing().when(taskService).save(isA(Task.class));
        when(repository.getByGroups(any())).thenReturn(emptyList());
        when(repository.get(any())).thenReturn(tasks);

        taskService.save(task);

        verify(taskService).save(task);

        assertAll(() -> service.save(task));
    }

    private static Task createTask() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Task task = new Task();
        task.setId(TASK_ID);
        task.setName("Task 1");
        task.setDescription("Some meeting");
        task.setDateCreate(LocalDate.parse("2012-08-10", formatter));
        task.setPosition(1);
        task.setGroupId(1);

        return task;
    }
}