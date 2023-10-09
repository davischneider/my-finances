package com.davi.myfinances.service.impl;

import com.davi.myfinances.exception.BusinessRuleException;
import com.davi.myfinances.model.entity.Launch;
import com.davi.myfinances.model.enums.LaunchStatus;
import com.davi.myfinances.model.enums.LaunchType;
import com.davi.myfinances.model.repository.LaunchRepository;
import com.davi.myfinances.service.LaunchService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LaunchServiceImpl implements LaunchService {

    LaunchRepository launchRepository;

    public LaunchServiceImpl(LaunchRepository launchRepository) {
        this.launchRepository = launchRepository;
    }

    @Override
    @Transactional
    public Launch save(Launch launch) {
        validate(launch);
        launch.setStatus(LaunchStatus.PENDENTE);
        return launchRepository.save(launch);
    }

    @Override
    @Transactional
    public Launch update(Launch launch) {
        validate(launch);
        Objects.requireNonNull(launch.getId());
        return launchRepository.save(launch);
    }

    @Override
    @Transactional
    public void delete(Launch launch) {
        Objects.requireNonNull(launch.getId());
        launchRepository.delete(launch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Launch> search(Launch launchFilters) {
        Example<Launch> example = Example.of(launchFilters, ExampleMatcher.matching()
                                                    .withIgnoreCase()
                                                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return launchRepository.findAll(example);
    }

    @Override
    public void updateStatus(Launch launch, LaunchStatus status) {
        launch.setStatus(status);
        update(launch);
    }

    @Override
    public void validate(Launch launch) {
        if (launch.getDescription() == null || launch.getDescription().trim().equals("")) {
            throw new BusinessRuleException("Informe uma descrição válida!");
        }
        if (launch.getMonth() == null || (launch.getMonth() < 1 || launch.getMonth() > 12)) {
            throw new BusinessRuleException("Informe um mês válido!");
        }
        if (launch.getYear() == null || launch.getYear().toString().length() != 4) {
            throw new BusinessRuleException("Informe um ano válido!");
        }
        if (launch.getUser() == null || launch.getUser().getId() == null) {
            throw new BusinessRuleException("Informe um usuário!");
        }
        if (launch.getValue() == null || launch.getValue().compareTo(BigDecimal.ZERO) < 1) {
            throw new BusinessRuleException("Informe um valor válido!");
        }
        if (launch.getType() == null) {
            throw new BusinessRuleException("Informe um tipo de lançamento!");
        }
    }

    @Override
    public Optional<Launch> getById(Long id) {
        return launchRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalanceByUser(Long userId) {
        BigDecimal revenues = launchRepository.getBalanceByTypeAndUser(userId, LaunchType.RECEITA);
        BigDecimal expenses = launchRepository.getBalanceByTypeAndUser(userId, LaunchType.DESPESA);

        if (revenues == null) {
            revenues = BigDecimal.ZERO;
        }

        if (expenses == null) {
            expenses = BigDecimal.ZERO;
        }

        return revenues.subtract(expenses);
    }
}
