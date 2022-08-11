package com.uapp.similartrello.mapper;

import com.uapp.similartrello.model.Task;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TaskMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        final Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setName(rs.getString("name"));
        task.setDescription(rs.getString("description"));
        task.setDateCreate(rs.getDate("date_create").toLocalDate());
        task.setPosition(rs.getInt("position"));
        task.setGroupId(rs.getInt("group_id"));

        return task;
    }
}