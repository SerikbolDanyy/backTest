package com.solva.backTest.services;

import com.solva.backTest.entities.Limit;
import com.solva.backTest.repositories.LimitRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LimitService {

    private final LimitRepository limitRepository;

    public LimitService(LimitRepository limitRepository) {
        this.limitRepository = limitRepository;
    }

    public List<Limit> getAllLimits() {
        return limitRepository.findAll();
    }

    public Limit setNewLimit(double limit) {
        Limit limit1 = new Limit();
        limit1.setDateTime(LocalDateTime.now());
        limit1.setLimitSum(limit);
        limit1.setCurrency("USD");
        return limitRepository.save(limit1);
    }

    public boolean isLimitExceeded(double amount) {
        Limit currentLimit = limitRepository.findTopByOrderByDateTimeDesc();
        return amount > currentLimit.getLimitSum();
    }
}
