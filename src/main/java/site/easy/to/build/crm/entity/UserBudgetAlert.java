package site.easy.to.build.crm.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_budget_alert")
public class UserBudgetAlert {

    @Column(name ="userId")
    private Long user_id;

    private BigDecimal totalBudget;
    private BigDecimal totalDepense;
    private BigDecimal alertThresholdValue;
    private Boolean alertTriggered;

    // Getters et Setters
    @Id
    public Long getUserId() {
        return user_id;
    }

    public void setUserId(Long userId) {
        this.user_id = userId;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public BigDecimal getTotalDepense() {
        return totalDepense;
    }

    public void setTotalDepense(BigDecimal totalDepense) {
        this.totalDepense = totalDepense;
    }

    public BigDecimal getAlertThresholdValue() {
        return alertThresholdValue;
    }

    public void setAlertThresholdValue(BigDecimal alertThresholdValue) {
        this.alertThresholdValue = alertThresholdValue;
    }

    public Boolean getAlertTriggered() {
        return alertTriggered;
    }

    public void setAlertTriggered(Boolean alertTriggered) {
        this.alertTriggered = alertTriggered;
    }
}
