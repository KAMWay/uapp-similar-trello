package com.uapp.similartrello.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class TaskForm {

    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @PositiveOrZero
    private int position;
    @Positive
    @NotNull
    private Integer groupId;
}
