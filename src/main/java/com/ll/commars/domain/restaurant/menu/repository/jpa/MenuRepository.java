package com.ll.commars.domain.restaurant.menu.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.restaurant.menu.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

}
