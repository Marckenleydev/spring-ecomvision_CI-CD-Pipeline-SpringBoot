package marc.dev.dashoard.service.impl;

import marc.dev.dashoard.entity.OverallStatEntity;
import marc.dev.dashoard.repository.OverallstatRepository;
import marc.dev.dashoard.service.OverallstatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverallstatServiceImpl implements OverallstatService {
    @Autowired
    OverallstatRepository overallstatRepository;

    @Override
    public List<OverallStatEntity> getAllOverallstats() {
        return overallstatRepository.findAll();
    }
}
