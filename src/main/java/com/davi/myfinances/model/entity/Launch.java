package com.davi.myfinances.model.entity;

import com.davi.myfinances.model.enums.LaunchStatus;
import com.davi.myfinances.model.enums.LaunchType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "launch")
@Builder
@Data
public class Launch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String description;

    @Column
    private Integer month;

    @Column
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User user;

    @Column
    private BigDecimal value;

    @Column(name = "register_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate registerDate;

    @Column
    @Enumerated(value = EnumType.STRING)
    private LaunchType type;

    @Column
    @Enumerated(value = EnumType.STRING)
    private LaunchStatus status;

}
