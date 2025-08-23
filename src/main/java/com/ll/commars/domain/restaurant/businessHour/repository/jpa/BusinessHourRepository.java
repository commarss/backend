package com.ll.commars.domain.restaurant.businessHour.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.restaurant.businessHour.entity.BusinessHour;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;

public interface BusinessHourRepository extends JpaRepository<BusinessHour, Long> {

	List<BusinessHour> findAllByRestaurant(Restaurant restaurant);
}
