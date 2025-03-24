package site.easy.to.build.crm.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "client_stat")
public class ClientStat {

    @Id
    @Column(name = "user_id")
    private Long userId;

    private String name;

    @Column(name = "total_budget")
    private Double totalBudget;

    @Column(name = "total_depense")
    private Double totalDepense;

    public ClientStat() {}

    public ClientStat(Long userId, String name, Double totalBudget, Double totalDepense) {
        this.userId = userId;
        this.name = name;
        this.totalBudget = totalBudget;
        this.totalDepense = totalDepense;
    }

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getTotalBudget() { return totalBudget; }
    public void setTotalBudget(Double totalBudget) { this.totalBudget = totalBudget; }

    public Double getTotalDepense() { return totalDepense; }
    public void setTotalDepense(Double totalDepense) { this.totalDepense = totalDepense; }
}
