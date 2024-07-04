package marc.dev.dashoard.service.impl;

import marc.dev.dashoard.dto.DailyData;
import marc.dev.dashoard.dto.DashboardStatsDto;
import marc.dev.dashoard.dto.MonthlyData;
import marc.dev.dashoard.entity.OverallStatEntity;
import marc.dev.dashoard.entity.TransactionEntity;
import marc.dev.dashoard.repository.OverallstatRepository;
import marc.dev.dashoard.repository.TransactionRepository;
import marc.dev.dashoard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OverallstatRepository overallStatRepository;
    @Override
    public DashboardStatsDto getDashboardStats() {
        String currentMonth = "November";
        int currentYear = 2021;
        String currentDay = "2021-11-15";

        List<TransactionEntity> transactions = transactionRepository.findTop50ByOrderByCreatedAtDesc();
        List<OverallStatEntity> overallStats = overallStatRepository.findByYear(currentYear);

        if(overallStats.isEmpty()){
             throw new RuntimeException("Overall stats not found for the year " + currentYear);
        }
        OverallStatEntity overallStat = overallStats.get(0);

        MonthlyData thisMonthStats = overallStat.getMonthlyData().stream()
                .filter(monthlyData -> monthlyData.getMonth().equals(currentMonth))
                .findFirst()
                .orElse(null);

        DailyData todayStats = overallStat.getDailyData().stream()
                .filter(dailyData -> dailyData.getDate().equals(currentDay))
                .findFirst()
                .orElse(null);


        return new DashboardStatsDto(
                overallStat.getTotalCustomers(),
                overallStat.getYearlyTotalSoldUnits(),
                overallStat.getYearlySalesTotal(),
                overallStat.getMonthlyData(),
                overallStat.getSalesByCategory(),
                thisMonthStats,
                todayStats,
                transactions
        );
    }
}
