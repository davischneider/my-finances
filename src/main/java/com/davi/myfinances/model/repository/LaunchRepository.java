package com.davi.myfinances.model.repository;

import com.davi.myfinances.model.entity.Launch;
import com.davi.myfinances.model.enums.LaunchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface LaunchRepository extends JpaRepository<Launch, Long> {

    @Query(value = "select sum(l.value) from Launch l join l.user u "
                    + "where u.id = :userId and l.type = :type group by u")
    BigDecimal getBalanceByTypeAndUser(@Param("userId") Long userId, @Param("type") LaunchType type);
}
