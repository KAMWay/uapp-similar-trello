package com.uapp.similartrello.repository;

import com.uapp.similartrello.mapper.GroupMapper;
import com.uapp.similartrello.model.Group;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class GroupRepositoryImpl implements GroupRepository {

    private final String SQL_SELECT_ALL = "SELECT id, name, position FROM groups";
    private final String SQL_SELECT = "SELECT id, name, position FROM groups WHERE id IN (:ids)";
    private final String SQL_DELETE = "DELETE FROM groups WHERE id = :id";
    private final String SQL_INSERT = "INSERT INTO groups (name, position) VALUES (:name, :position)";
    private final String SQL_UPDATE = "UPDATE groups SET name=:name, position=:position WHERE id=:id";
    private final String SQL_UPDATE_POSITION = "UPDATE groups SET position=:position WHERE id=:id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final GroupMapper mapper;

    public GroupRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate, GroupMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public List<Group> get(@NonNull Collection<Integer> ids) {
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource("ids", ids);

        return jdbcTemplate.query(SQL_SELECT, parameterSource, mapper);
    }

    @Override
    public List<Group> getAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, mapper);
    }

    @Override
    public void delete(Group group) {
        jdbcTemplate.update(SQL_DELETE, new MapSqlParameterSource("id", group.getId()));
    }

    @Override
    public void save(Group group) {
        if (group.getId() == null) {
            insert(group);
        } else {
            update(group);
        }
    }

    @Override
    public void updatePosition(List<Group> groups) {
        final Map<String, Integer>[] parameters = new Map[groups.size()];
        for (int i = 0; i < groups.size(); i++) {
            final Map<String, Integer> newMap = new LinkedHashMap<>();
            final Group group = groups.get(i);
            newMap.put("id", group.getId());
            newMap.put("position", group.getPosition());
            parameters[i] = newMap;
        }

        jdbcTemplate.batchUpdate(SQL_UPDATE_POSITION, parameters);
    }

    private void insert(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL_INSERT, getSource(group), keyHolder, new String[]{"id"});
        int id = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        group.setId(id);
    }

    private void update(Group group) {
        jdbcTemplate.update(SQL_UPDATE, getSource(group));
    }

    private MapSqlParameterSource getSource(Group group) {
        return new MapSqlParameterSource("id", group.getId())
                .addValue("name", group.getName())
                .addValue("position", group.getPosition());
    }
}
