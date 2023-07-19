package com.davi.myfinances.model.repository;

import com.davi.myfinances.model.entity.Launch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaunchRepository extends JpaRepository<Launch, Long> {
}
