package com.ll.commars.domain.auth.authUserInfo.repository;

import com.ll.commars.domain.auth.authUserInfo.entity.AuthUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserInfoRepository extends JpaRepository<AuthUserInfo, Long> {
}
