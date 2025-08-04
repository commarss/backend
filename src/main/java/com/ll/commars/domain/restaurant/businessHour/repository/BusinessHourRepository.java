package com.ll.commars.domain.restaurant.businessHour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ll.commars.domain.restaurant.businessHour.entity.BusinessHour;

@Repository
public interface BusinessHourRepository extends JpaRepository<BusinessHour, Long> {

}
