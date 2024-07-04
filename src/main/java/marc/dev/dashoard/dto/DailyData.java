package marc.dev.dashoard.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Getter
@Setter
public class DailyData {
    @Field("date")
    private String date;

    @Field("totalSales")
    private double totalSales;

    @Field("totalUnits")
    private int totalUnits;
}
