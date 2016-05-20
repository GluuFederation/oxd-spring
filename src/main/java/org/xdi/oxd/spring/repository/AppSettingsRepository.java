package org.xdi.oxd.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xdi.oxd.spring.domain.AppSettings;
import org.xdi.oxd.spring.domain.enumiration.SettingsType;

public interface AppSettingsRepository extends JpaRepository<AppSettings, Integer> {
    AppSettings findOneByType(SettingsType type);
}
