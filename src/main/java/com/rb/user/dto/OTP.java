package com.rb.user.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "temp_otps")
@Data
public class OTP {
    @Id
    private String id;
    private Integer otp;
    private String belongsTo;
    private Long expiresAt;
}
