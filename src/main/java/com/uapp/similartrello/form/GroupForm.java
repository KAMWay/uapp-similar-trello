package com.uapp.similartrello.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class GroupForm {

    private Integer id;
    @NotNull
    private String name;
    @PositiveOrZero
    private int position;
}
