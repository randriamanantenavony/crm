package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.easy.to.build.crm.entity.TicketStatusCount;

public interface TicketStatutRepository extends JpaRepository<TicketStatusCount, String> {
    
}
