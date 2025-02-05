package com.ll.commars.domain.restaurant.businessHour.repository;

import com.ll.commars.domain.restaurant.businessHour.entity.BusinessHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessHourRepository extends JpaRepository<BusinessHour, Long> {

}
