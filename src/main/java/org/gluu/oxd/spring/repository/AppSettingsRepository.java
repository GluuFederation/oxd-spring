package org.gluu.oxd.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.gluu.oxd.spring.domain.AppSettings;

public interface AppSettingsRepository extends JpaRepository<AppSettings, Integer> {
    AppSettings findOneByOpHost(String opHost);
}
