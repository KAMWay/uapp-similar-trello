package com.uapp.similartrello.service.logic;

import com.uapp.similartrello.model.Task;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class TaskPositionCalculationService implements PositionCalculationService<Task> {

    @Override
    public int getUpdatePosition(int listSize, Task task) {
        if (task.getPosition() == 0) {
            return listSize + (task.getId() == null ? 1 : 0);
        }

        return listSize == 0 || task.getPosition() > listSize ? 1 : task.getPosition();
    }

    @Override
    public List<Task> getList4UpdatePositionIfDelete(List<Task> tasks, Task task) {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getPosition))
                .filter(e -> e.getPosition() > task.getPosition())
                .peek(e -> e.setPosition(e.getPosition() - 1))
                .toList();
    }

    @Override
    public List<Task> getList4UpdatePositionIfSave(List<Task> tasks, Task task) {
        if (task.getId() == null) {

            return tasks.stream()
                    .sorted(Comparator.comparing(Task::getPosition).reversed())
                    .filter(e -> e.getPosition() >= task.getPosition())
                    .peek(e -> e.setPosition(e.getPosition() + 1))
                    .toList();
        }

        final Task curTask = tasks.stream()
                .filter(e -> Objects.equals(e.getId(), task.getId()))
                .findFirst()
                .orElse(null);

        final int fromIndex = curTask == null ? tasks.size() + 1 : curTask.getPosition();
        final int steep = fromIndex < task.getPosition() ? -1 : 1;
        final List<Task> subList = getSubList4Update(
                tasks.stream().sorted(Comparator.comparing(Task::getPosition)).toList(),
                fromIndex,
                task.getPosition()
        );

        return subList.stream()
                .peek(e -> e.setPosition(e.getPosition() + steep))
                .toList();
    }

    private List<Task> getSubList4Update(List<Task> tasks, int fromIndex, int toIndex) {
        if (fromIndex == toIndex || tasks.isEmpty()) {
            return Collections.emptyList();
        }

        return fromIndex < toIndex
                ? tasks.subList(fromIndex, toIndex)
                : tasks.subList(toIndex - 1, fromIndex - 1).stream()
                .sorted(Comparator.comparing(Task::getPosition).reversed())
                .toList();
    }
}
