package com.ll.commars.domain.restaurant.businessHour.service;

import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourDto;
import com.ll.commars.domain.restaurant.businessHour.repository.BusinessHourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessHourService {
    private final BusinessHourRepository businessHourRepository;

    @Transactional
    public void truncate() {
        businessHourRepository.deleteAll();
    }
}
