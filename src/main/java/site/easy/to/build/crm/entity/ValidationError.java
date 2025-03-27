package site.easy.to.build.crm.entity;

import lombok.Data;

@Data
public class ValidationError {
    private int rowNumber;
    private String tableName;
    private String message;

    public ValidationError(int rowNumber, String tableName, String message) {
        this.rowNumber = rowNumber;
        this.tableName = tableName;
        this.message = message;
    }

}
