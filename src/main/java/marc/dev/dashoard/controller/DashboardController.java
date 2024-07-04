package marc.dev.dashoard.controller;

import marc.dev.dashoard.dto.DashboardStatsDto;
import marc.dev.dashoard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")

public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public DashboardStatsDto getDashboardStats() {
        return dashboardService.getDashboardStats();
    }
}
