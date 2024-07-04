package marc.dev.dashoard.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import marc.dev.dashoard.dto.DailyData;
import marc.dev.dashoard.dto.MonthlyData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "productstats")
@Setter
@Getter
public class ProductStatEntity {

    @Id
    private String id;

    @Field("productId")
    private String productId;

    @Field("yearlySalesTotal")
    private double yearlySalesTotal;

    @Field("yearlyTotalSoldUnits")
    private int yearlyTotalSoldUnits;

    @Field("year")
    private int year;

    @Field("monthlyData")
    private List<MonthlyData> monthlyData;

    @Field("dailyData")
    private List<DailyData> dailyData;

    @Field("createdAt")
    private LocalDateTime createdAt;

    @Field("updatedAt")
    private LocalDateTime updatedAt;


}
