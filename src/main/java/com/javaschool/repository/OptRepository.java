package com.javaschool.repository;

import com.javaschool.entity.user.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptRepository extends JpaRepository<UserOtp,Long> {

    Optional<UserOtp> findByUserEmail(String email);

}
