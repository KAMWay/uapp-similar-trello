package com.uapp.similartrello.repository;

import com.uapp.similartrello.model.Group;

import java.util.Collection;
import java.util.List;

public interface GroupRepository {

    List<Group> get(Collection<Integer> ids);

    List<Group> getAll();

    void delete(Group group);

    void save(Group group);

    void updatePosition(List<Group> groups);
}
