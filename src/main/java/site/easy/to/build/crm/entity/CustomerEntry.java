package site.easy.to.build.crm.entity;

import lombok.Data;

@Data
public class CustomerEntry {
    private String customerEmail;
    private String subjectOrName;
    private String type;
    private String status;
    private double expense;

    public CustomerEntry(String customerEmail, String subjectOrName, String type, String status, double expense) {
        this.customerEmail = customerEmail;
        this.subjectOrName = subjectOrName;
        this.type = type;
        this.status = status;
        this.expense = expense;
    }

    @Override
    public String toString() {
        return "CustomerEntry{" +
                "customerEmail='" + customerEmail + '\'' +
                ", subjectOrName='" + subjectOrName + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", expense=" + expense +
                '}';
    }

    // Getters et Setters si nécessaire
}

