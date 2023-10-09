package com.davi.myfinances.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LaunchDTO {

    private Long id;
    private String description;
    private Integer month;
    private Integer year;
    private BigDecimal value;
    private long user;
    private String type;
    private String status;
}
