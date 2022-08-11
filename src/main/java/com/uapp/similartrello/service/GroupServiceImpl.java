package com.uapp.similartrello.service;

import com.uapp.similartrello.model.Group;
import com.uapp.similartrello.repository.GroupRepository;
import com.uapp.similartrello.service.logic.PositionCalculationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository repository;
    private final PositionCalculationService<Group> calculationService;

    public GroupServiceImpl(GroupRepository repository, PositionCalculationService<Group> calculationService) {
        this.repository = repository;
        this.calculationService = calculationService;
    }

    @Override
    public List<Group> get(Collection<Integer> ids) {
        return repository.get(ids);
    }

    @Override
    public List<Group> getAll() {
        return repository.getAll();
    }

    @Override
    @Transactional
    public void delete(Group group) {
        final List<Group> groups = getAll();
        repository.delete(group);

        final List<Group> updateGroups = calculationService.getList4UpdatePositionIfDelete(groups, group);
        updateGroupsPosition(updateGroups);
    }

    @Override
    @Transactional
    public void save(Group group) {
        final List<Group> groups = repository.getAll();
        group.setPosition(calculationService.getUpdatePosition(groups.size(), group));

        final LinkedList<Group> updateGroups = new LinkedList<>(calculationService.getList4UpdatePositionIfSave(groups, group));
        if (group.getId() == null) {
            updateGroupsPosition(updateGroups);
        } else {
            updateGroupsPosition(updateGroups, group);
        }

        repository.save(group);
    }

    private void updateGroupsPosition(List<Group> updateGroups, Group tmpGroup) {
        final int tmp = tmpGroup.getPosition();
        tmpGroup.setPosition(0);
        updateGroups.add(0, tmpGroup);
        updateGroupsPosition(updateGroups);
        tmpGroup.setPosition(tmp);
    }

    private void updateGroupsPosition(List<Group> groups) {
        if (!groups.isEmpty()) {
            repository.updatePosition(groups);
        }
    }
}
