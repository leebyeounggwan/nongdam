package com.example.nongdam.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Hello {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long testId;

    private String text;
}
