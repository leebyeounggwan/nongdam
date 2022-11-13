package com.example.nongdam.entity.crop.entity;

import com.example.nongdam.entity.TimeStamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "customUnique", columnNames = {"type", "category", "kind"})
        }
)
public class Crop extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int category;

    @Column
    private int type;

    @Column
    private String kind;

    @Column
    private String name;
}
