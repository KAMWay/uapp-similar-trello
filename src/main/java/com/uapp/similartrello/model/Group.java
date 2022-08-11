package com.uapp.similartrello.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Group {

    private Integer id;
    private String name;
    private int position;
}
