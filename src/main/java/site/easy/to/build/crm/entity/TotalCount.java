package site.easy.to.build.crm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TotalCount {

    @Id
    private Long id = 1L; // Nous avons besoin d'un id unique pour l'entité

    private long customerCount;
    private long leadCount;
    private long ticketCount;

    // Getters et setters

    public long getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(long customerCount) {
        this.customerCount = customerCount;
    }

    public long getLeadCount() {
        return leadCount;
    }

    public void setLeadCount(long leadCount) {
        this.leadCount = leadCount;
    }

    public long getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(long ticketCount) {
        this.ticketCount = ticketCount;
    }
}
