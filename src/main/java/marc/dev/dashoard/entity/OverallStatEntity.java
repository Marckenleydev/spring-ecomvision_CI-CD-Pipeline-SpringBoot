package marc.dev.dashoard.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import marc.dev.dashoard.dto.DailyData;
import marc.dev.dashoard.dto.MonthlyData;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "overallstats")
@Setter
@Getter
public class OverallStatEntity {
    @Id
    private String id;

    @Field("totalCustomers")
    private Integer totalCustomers;

    @Field("yearlySalesTotal")
    private Integer yearlySalesTotal;

    @Field("yearlyTotalSoldUnits")
    private Integer yearlyTotalSoldUnits;

    @Field("year")
    private Integer year;

    @Field("monthlyData")
    public  List<MonthlyData> monthlyData;

    @Field("dailyData")
    private List<DailyData> dailyData;

    @Field("salesByCategory")
    private Map<String, Integer> salesByCategory;

    @CreatedDate
    @Field("createdAt")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updatedAt")
    private LocalDateTime updatedAt;
}
