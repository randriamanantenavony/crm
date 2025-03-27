package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import site.easy.to.build.crm.entity.Depense;

public interface DepenseRepository extends JpaRepository<Depense, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Depense d WHERE d.ticket.ticketId = :ticketId")
    void deleteByTicketId(@Param("ticketId") Integer ticketId);


    @Modifying
    @Transactional
    @Query("DELETE FROM Depense d WHERE d.lead.leadId = :leadId")
    void deleteByLeadId(@Param("leadId") Integer leadId);

    @Modifying
    @Transactional
    @Query("UPDATE Depense d SET d.montant = :montant WHERE d.ticket.ticketId = :ticketId")
    void updateMontantByTicketId(@Param("montant") double montant, @Param("ticketId") int ticketId);
    

    @Modifying
    @Transactional
    @Query("UPDATE Depense d SET d.montant = :montant WHERE d.lead.leadId = :leadId")
    void updateMontantByLeadId(@Param("montant") double montant, @Param("leadId") int leadId);


}

