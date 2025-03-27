package site.easy.to.build.crm.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TempLeadTicket {
    private String customerEmail;
    private String subjectOrName;
    private String type;
    private String status;
    private BigDecimal expense;

    // Getters, Setters
}

