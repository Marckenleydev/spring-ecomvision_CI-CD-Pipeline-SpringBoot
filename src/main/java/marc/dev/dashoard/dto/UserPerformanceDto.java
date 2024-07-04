package marc.dev.dashoard.dto;

import lombok.Data;
import marc.dev.dashoard.entity.TransactionEntity;
import marc.dev.dashoard.entity.UserEntity;

import java.util.List;

@Data
public class UserPerformanceDto {
    private UserEntity user;
    private List<TransactionEntity> sales;

    public UserPerformanceDto(UserEntity user, List<TransactionEntity> sales) {
        this.user = user;
        this.sales = sales;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
    public List<TransactionEntity> get(){
        return sales;
    }

    public void setSales(List<TransactionEntity> sales) {
        this.sales = sales;
    }
}
