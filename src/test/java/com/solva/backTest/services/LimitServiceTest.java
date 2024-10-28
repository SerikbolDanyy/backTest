package com.solva.backTest.services;

import com.solva.backTest.entities.Limit;
import com.solva.backTest.repositories.LimitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class LimitServiceTest {

    private LimitRepository repository;
    private LimitService limitService;

    @BeforeEach
    public void setUp() {
        repository = Mockito.mock(LimitRepository.class);
        limitService = new LimitService(repository);
    }

    @Test
    public void testIsLimitExceeded() {
        Limit limit = new Limit();
        limit.setLimitSum(1000.0);
        Mockito.when(repository.findTopByOrderByDateTimeDesc()).thenReturn(limit);

        boolean exceeded = limitService.isLimitExceeded(1500.0);

        assertThat(exceeded).isTrue();
    }

    @Test
    public void testSetNewLimit() {
        Limit mockLimit = new Limit();
        mockLimit.setLimitSum(2000.0);
        mockLimit.setCurrency("USD");

        Mockito.when(repository.save(any(Limit.class))).thenReturn(mockLimit);

        Limit newLimit = limitService.setNewLimit(2000.0);

        assertThat(newLimit).isNotNull();
        assertThat(newLimit.getLimitSum()).isEqualTo(2000.0);
        assertThat(newLimit.getCurrency()).isEqualTo("USD");
    }
}

