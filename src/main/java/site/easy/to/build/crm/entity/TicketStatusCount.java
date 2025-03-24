package site.easy.to.build.crm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket_status_counts") // Correspond au nom de la vue
public class TicketStatusCount {

    @Id
    private String status;  // Le statut des tickets

    @Column(name = "ticket_count")
    private long ticketCount;  // Le nombre de tickets pour chaque statut

    // Constructeur, getters et setters
    public TicketStatusCount() {}

    public TicketStatusCount(String status, long ticketCount) {
        this.status = status;
        this.ticketCount = ticketCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(long ticketCount) {
        this.ticketCount = ticketCount;
    }
}
