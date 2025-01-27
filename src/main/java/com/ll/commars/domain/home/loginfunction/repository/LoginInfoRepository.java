package com.ll.commars.domain.home.loginfunction.repository;

import com.ll.commars.domain.home.board.entity.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {
}
