package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.easy.to.build.crm.entity.TotalCount;

public interface TotalCountRepository extends JpaRepository<TotalCount, Long> {
}
