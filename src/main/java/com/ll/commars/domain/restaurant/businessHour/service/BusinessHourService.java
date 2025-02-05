package com.ll.commars.domain.restaurant.businessHour.service;

import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourDto;
import com.ll.commars.domain.restaurant.businessHour.repository.BusinessHourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessHourService {
    private final BusinessHourRepository businessHourRepository;
}
