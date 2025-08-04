package com.ll.commars.domain.restaurant.businessHour.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.restaurant.businessHour.entity.BusinessHour;

public interface BusinessHourRepository extends JpaRepository<BusinessHour, Long> {

}
