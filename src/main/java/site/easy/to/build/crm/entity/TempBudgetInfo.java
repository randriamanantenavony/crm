package site.easy.to.build.crm.entity;


import java.math.BigDecimal;

import lombok.Data;

@Data
public class TempBudgetInfo {
    private String customerEmail;
    private BigDecimal budget;

}

