package com.davi.myfinances.model.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "user", schema = "finances")
@Builder
@Data
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;


}
