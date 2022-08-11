package com.uapp.similartrello.service.logic;

import com.uapp.similartrello.exception.NotFoundException;
import com.uapp.similartrello.model.Group;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GroupPositionCalculationService implements PositionCalculationService<Group> {

    @Override
    public int getUpdatePosition(int listSize, Group group) {
        if (group.getPosition() == 0) {
            return listSize + (group.getId() == null ? 1 : 0);
        }

        return listSize == 0 ? 1 : group.getPosition();
    }

    @Override
    public List<Group> getList4UpdatePositionIfDelete(List<Group> groups, Group group) {
        return groups.stream()
                .sorted(Comparator.comparing(Group::getPosition))
                .filter(e -> e.getPosition() > group.getPosition())
                .peek(e -> e.setPosition(e.getPosition() - 1))
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> getList4UpdatePositionIfSave(List<Group> groups, Group group) {
        if (group.getId() == null) {

            return groups.stream()
                    .sorted(Comparator.comparing(Group::getPosition).reversed())
                    .filter(e -> e.getPosition() >= group.getPosition())
                    .peek(e -> e.setPosition(e.getPosition() + 1))
                    .toList();
        }

        final int fromIndex = groups.stream()
                .filter(e -> Objects.equals(e.getId(), group.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found group by id " + group.getId()))
                .getPosition();

        final List<Group> subList = getSubList4Update(
                groups.stream().sorted(Comparator.comparing(Group::getPosition)).toList(),
                fromIndex, group.getPosition()
        );
        final int steep = fromIndex < group.getPosition() ? -1 : 1;

        return subList.stream()
                .peek(e -> e.setPosition(e.getPosition() + steep))
                .toList();
    }

    private List<Group> getSubList4Update(List<Group> groups, int fromIndex, int toIndex) {
        return fromIndex < toIndex
                ? groups.subList(fromIndex, toIndex)
                : groups.subList(toIndex - 1, fromIndex - 1).stream()
                .sorted(Comparator.comparing(Group::getPosition).reversed())
                .toList();
    }
}
