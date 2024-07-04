package marc.dev.dashoard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import marc.dev.dashoard.entity.TransactionEntity;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class DashboardStatsDto {
    private int totalCustomer;
    private int yearlyTotalSoldUnits;
    private int yearlySalesTotal;
    private List<MonthlyData> monthlyData;
    private Map<String, Integer> salesByCategory;
    private MonthlyData thisMonthlyData;
    private DailyData thisDailyData;
    private List<TransactionEntity> transactions;
}
