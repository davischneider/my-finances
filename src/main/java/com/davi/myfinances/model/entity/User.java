package com.davi.myfinances.model.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "user")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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
