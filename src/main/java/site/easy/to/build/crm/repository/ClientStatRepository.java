package site.easy.to.build.crm.repository;

import site.easy.to.build.crm.entity.ClientStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientStatRepository extends JpaRepository<ClientStat, Long> {
}
