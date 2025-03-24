package site.easy.to.build.crm.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import site.easy.to.build.crm.entity.GlobalSettings;

public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Integer> {
}
