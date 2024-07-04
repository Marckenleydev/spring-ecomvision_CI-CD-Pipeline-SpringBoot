package marc.dev.dashoard.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;
@Data
@Getter
@Setter
public class MonthlyData {
    @Field("month")
    private String month;

    @Field("totalSales")
    private int totalSales;

    @Field("totalUnits")
    private int totalUnits;
}
