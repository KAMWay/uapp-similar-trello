package com.uapp.similartrello.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Task {

    private Integer id;
    private String name;
    private String description;
    private LocalDate dateCreate;
    private int position;
    private Integer groupId;
}
