package com.uapp.similartrello.service;

import com.uapp.similartrello.model.Group;

import java.util.Collection;
import java.util.List;

public interface GroupService {

    List<Group> get(Collection<Integer> ids);

    List<Group> getAll();

    void delete(Group group);

    void save(Group group);
}
