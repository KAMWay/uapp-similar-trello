package com.uapp.similartrello.repository;

import com.uapp.similartrello.mapper.TaskMapper;
import com.uapp.similartrello.model.Task;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.*;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final String SQL_SELECT_ALL = "SELECT id, name, description, date_create, position, group_id FROM task";
    private final String SQL_SELECT = "SELECT id, name, description, date_create, position, group_id FROM task WHERE id IN (:ids)";
    private final String SQL_SELECT_BY_GROUP_ID = "SELECT id, name, description, date_create, position, group_id FROM task WHERE group_id IN (:ids)";
    private final String SQL_DELETE = "DELETE FROM task WHERE id = :id";
    private final String SQL_INSERT = "INSERT INTO task (name, description, date_create, position, group_id) VALUES (:name, :description, :date_create, :position, :group_id)";
    private final String SQL_UPDATE = "UPDATE task SET name=:name, description=:description, position=:position, group_id=:group_id WHERE id=:id";
    private final String SQL_UPDATE_POSITION = "UPDATE task SET position=:position WHERE id=:id";
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TaskMapper mapper;

    public TaskRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate, TaskMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public List<Task> get(@NonNull Collection<Integer> ids) {
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource("ids", ids);

        return jdbcTemplate.query(SQL_SELECT, parameterSource, mapper);
    }

    @Override
    public List<Task> getByGroups(@NonNull Collection<Integer> ids) {
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource("ids", ids);

        return jdbcTemplate.query(SQL_SELECT_BY_GROUP_ID, parameterSource, mapper);
    }

    @Override
    public List<Task> getAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, mapper);
    }

    @Override
    public void delete(Task task) {
        jdbcTemplate.update(SQL_DELETE, new MapSqlParameterSource("id", task.getId()));
    }

    @Override
    public void save(Task task) {
        if (task.getId() == null) {
            insert(task);
        } else {
            update(task);
        }
    }

    @Override
    public void updatePosition(List<Task> tasks) {
        final Map<String, Integer>[] parameters = new Map[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            final Map<String, Integer> newMap = new LinkedHashMap<>();
            final Task task = tasks.get(i);
            newMap.put("id", task.getId());
            newMap.put("position", task.getPosition());
            parameters[i] = newMap;
        }

        jdbcTemplate.batchUpdate(SQL_UPDATE_POSITION, parameters);
    }

    private void insert(Task task) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL_INSERT, getSource(task), keyHolder, new String[]{"id"});
        int id = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        task.setId(id);
    }

    private void update(Task task) {
        jdbcTemplate.update(SQL_UPDATE, getSource(task));
    }

    private MapSqlParameterSource getSource(Task task) {
        return new MapSqlParameterSource("id", task.getId())
                .addValue("name", task.getName())
                .addValue("description", task.getDescription())
                .addValue("date_create", Date.valueOf(task.getDateCreate()))
                .addValue("position", task.getPosition())
                .addValue("group_id", task.getGroupId());
    }
}
