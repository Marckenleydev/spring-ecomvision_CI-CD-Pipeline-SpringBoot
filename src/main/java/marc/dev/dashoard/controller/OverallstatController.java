package marc.dev.dashoard.controller;

import marc.dev.dashoard.entity.OverallStatEntity;
import marc.dev.dashoard.service.OverallstatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/overall-stats")
public class OverallstatController {
    @Autowired
    private OverallstatService overallStatService;
    @GetMapping
    public ResponseEntity<List<OverallStatEntity>> getMonthlyData(){
        return new ResponseEntity<>(overallStatService.getAllOverallstats(),OK);
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test(){
        return ResponseEntity.ok().body(Map.of("Testing", "Up  running"));
    }


}
