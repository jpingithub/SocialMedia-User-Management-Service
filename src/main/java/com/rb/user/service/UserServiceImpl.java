package com.rb.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rb.user.client.NotificationClient;
import com.rb.user.dto.UserRequest;
import com.rb.user.dto.UserResponse;
import com.rb.user.entity.OTP;
import com.rb.user.entity.User;
import com.rb.user.exception.NoUserFoundException;
import com.rb.user.exception.UserException;
import com.rb.user.repo.OtpRepository;
import com.rb.user.repo.UserRepository;
import com.rb.user.util.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final NotificationClient notificationClient;
    private final OtpRepository otpRepository;

    @Value("${email.subject.text}")
    private String subjectText;

    @Override
    public UserResponse saveUser(UserRequest request) {
        if (request.getIsEmailVerified()) {
            final User user = objectMapper.convertValue(request, User.class);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            if (isAvailable(request.getUsername())) {
                final User savedUser = userRepository.save(user);
                log.info("User name is unique, data saved successfully");
                return Utilities.convertToUserResponse(savedUser);
            } else {
                log.info("User name already exist : {}", request.getUsername());
                throw new UserException("Username already exist.");
            }
        } else {
            log.info("Email not verified : {}", request.getEmail());
            throw new UserException("Please verify the email ");
        }
    }

    @Override
    public User findByUserName(String username) {
        final Optional<User> optionalUserFromDB = userRepository.findByUsername(username);
        if (optionalUserFromDB.isPresent()) {
            log.info("User found with the user name : {}", username);
            return optionalUserFromDB.get();
        } else {
            log.info("No user found with the user name : {}", username);
            throw new UserException("User not found with the user name : " + username);
        }
    }

    @Override
    public UserResponse updateUser(String userName, UserRequest userRequest) {
        final User byUserName = findByUserName(userName);
        if (userRequest.getFirstName() != null && !userRequest.getFirstName().equals(byUserName.getFirstName()))
            byUserName.setFirstName(userRequest.getFirstName());
        if (userRequest.getLastName() != null && !userRequest.getLastName().equals(byUserName.getLastName()))
            byUserName.setLastName(userRequest.getLastName());
        if (userRequest.getUsername() != null) {
            log.info("checking new username availability : {}", userRequest.getUsername());
            if (!userRequest.getUsername().equals(byUserName.getUsername())) {
                if (isAvailable(userRequest.getUsername())) {
                    log.info("Username available to update : {}", userRequest.getUsername());
                    byUserName.setUsername(userRequest.getUsername());
                } else {
                    log.info("User name not available to update");
                    throw new UserException("Username already exist, and cannot update it.");
                }
            } else log.info("Existed username is same as you provided now : {}", userRequest.getUsername());
        }
        if (userRequest.getPassword() != null) byUserName.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return Utilities.convertToUserResponse(userRepository.save(byUserName));
    }

    @Override
    public User searchUserByUsername(String userName) {
        final Optional<User> optionUser = userRepository.findByUsername(userName);
        if (optionUser.isPresent()) {
            log.info("User found with the username : {}", userName);
            return optionUser.get();
        } else {
            log.info("User not found with the username : {}", userName);
            throw new NoUserFoundException("User not found with the username : " + userName);
        }
    }

    @Override
    public OTP generateOTP(String email) {
        if (isAvailable(email)) {
            Integer otp = Utilities.getOtp();
            final OTP otpObject = new OTP();
            otpObject.setOtp(otp);
            otpObject.setBelongsTo(email);
            otpObject.setExpiresAt(System.currentTimeMillis() + (5 * 60000));
            otpRepository.save(otpObject);
            notificationClient.sendEmail(email, "Your OTP is : " + otp, subjectText);
            return otpObject;
        } else {
            log.info("User already exist with the email : {}", email);
            throw new UserException("User already exists with the email : " + email);
        }
    }

    @Override
    public Boolean validateOtp(String belongsTo, Integer otp) {
        Optional<OTP> validOtp = otpRepository.findByOtpAndBelongsTo(otp, belongsTo);
        if (validOtp.isPresent()) {
            OTP validatedOTP = validOtp.get();
            if (validatedOTP.getExpiresAt() > System.currentTimeMillis()) {
                log.info("OTP verified successfully");
                otpRepository.delete(validatedOTP);
                return true;
            } else {
                log.info("You have entered expired OTP : {}", otp);
                otpRepository.delete(validatedOTP);
                otpRepository.deleteByExpiresAtLessThan(System.currentTimeMillis());
                throw new UserException("OTP expired : " + otp);
            }
        } else {
            log.info("OTP verification failed");
            otpRepository.deleteByExpiresAtLessThan(System.currentTimeMillis());
            throw new UserException("You have entered invalid OTP");
        }
    }

    private Boolean isAvailable(String value) {
        boolean result = false;
        if (!value.endsWith("@gmail.com")) result = userRepository.findByUsername(value).isEmpty();
        else result = userRepository.findByEmail(value).isEmpty();
        return result;
    }

}
