package site.easy.to.build.crm.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "GlobalSettings")
public class GlobalSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int setting_id;

    @Column(nullable = false)
    private double alert_threshold;

    // Getters and setters
    public int getSetting_id() {
        return setting_id;
    }

    public void setSetting_id(int setting_id) {
        this.setting_id = setting_id;
    }

    public double getAlert_threshold() {
        return alert_threshold;
    }

    public void setAlert_threshold(double alert_threshold) {
        this.alert_threshold = alert_threshold;
    }
}
