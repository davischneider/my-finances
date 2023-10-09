package com.davi.myfinances.service;

import com.davi.myfinances.model.entity.Launch;
import com.davi.myfinances.model.enums.LaunchStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LaunchService {

    Launch save(Launch launch);

    Launch update(Launch launch);

    void delete(Launch launch);

    List<Launch> search(Launch launchFilters);

    void updateStatus(Launch launch, LaunchStatus status);

    void validate(Launch launch);

    Optional<Launch> getById(Long id);

    BigDecimal getBalanceByUser(Long userId);

}
