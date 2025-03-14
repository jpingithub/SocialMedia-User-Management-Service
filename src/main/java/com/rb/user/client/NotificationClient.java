package com.rb.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NOTIFICATION-SERVICE", url = "http://localhost:8080")
public interface NotificationClient {
    @PostMapping("/api/v1/emails")
    ResponseEntity<String> sendEmail(@RequestParam("to") String toEmail, @RequestParam("content") String content, @RequestParam("subject") String subject);
}
