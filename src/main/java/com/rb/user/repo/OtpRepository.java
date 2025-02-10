package com.rb.user.repo;

import com.rb.user.dto.OTP;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OtpRepository extends MongoRepository<OTP, String> {
    Optional<OTP> findByOtpAndBelongsTo(Integer otp,String belongsTo);
    @Transactional
    long deleteByExpiresAtLessThan(Long now);
}
