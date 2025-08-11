package com.ll.commars.domain.restaurant.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.restaurant.repository.jpa.BusinessHourRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessHourService {

	private final BusinessHourRepository businessHourRepository;

	@Transactional
	public void truncate() {
		businessHourRepository.deleteAll();
	}
}
