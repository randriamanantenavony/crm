package site.easy.to.build.crm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lead_status_counts") 
public class LeadStatusCount {
    
    
    @Id
    private String status;  // Le statut des tickets

    @Column(name = "lead_count")
    private long leadCount;  // Le nombre de tickets pour chaque statut

    // Constructeur, getters et setters
    public LeadStatusCount() {}

    public LeadStatusCount(String status, long leadCount) {
        this.status = status;
        this.leadCount = leadCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getleadCount() {
        return leadCount;
    }

    public void setleadCount(long leadCount) {
        this.leadCount = leadCount;
    }


}
