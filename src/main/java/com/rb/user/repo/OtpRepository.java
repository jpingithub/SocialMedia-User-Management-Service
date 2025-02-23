package com.rb.user.repo;

import com.rb.user.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OTP, String> {
    Optional<OTP> findByOtpAndBelongsTo(Integer otp,String belongsTo);
    @Transactional
    long deleteByExpiresAtLessThan(Long now);
}
