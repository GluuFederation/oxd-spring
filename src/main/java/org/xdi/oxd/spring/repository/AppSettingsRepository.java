package org.xdi.oxd.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xdi.oxd.spring.domain.AppSettings;

public interface AppSettingsRepository extends JpaRepository<AppSettings, Integer> {
    AppSettings findOneByOpHost(String opHost);
}
