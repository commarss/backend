package com.ll.commars.domain.restaurant.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.restaurant.entity.BusinessHour;

public interface BusinessHourRepository extends JpaRepository<BusinessHour, Long> {

}
