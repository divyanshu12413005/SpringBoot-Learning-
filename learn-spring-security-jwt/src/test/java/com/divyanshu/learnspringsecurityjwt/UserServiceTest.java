package com.divyanshu.learnspringsecurityjwt;

import com.divyanshu.learnspringsecurityjwt.dto.LoginResponse;
import com.divyanshu.learnspringsecurityjwt.dto.RegisterRequest;
import com.divyanshu.learnspringsecurityjwt.dto.UserResponse;
import com.divyanshu.learnspringsecurityjwt.entity.Role;
import com.divyanshu.learnspringsecurityjwt.entity.User;
import com.divyanshu.learnspringsecurityjwt.repository.UserRepository;
import com.divyanshu.learnspringsecurityjwt.service.CachedUserService;
import com.divyanshu.learnspringsecurityjwt.service.EmailService;
import com.divyanshu.learnspringsecurityjwt.service.JwtService;
import com.divyanshu.learnspringsecurityjwt.service.OtpService;
import com.divyanshu.learnspringsecurityjwt.service.RedisService;
import com.divyanshu.learnspringsecurityjwt.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.divyanshu.learnspringsecurityjwt.dto.LoginRequest;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private OtpService otpService;

    @Mock
    private EmailService emailService;

    @Mock
    private RedisService redisService;

    @Mock
    private CachedUserService cachedUserService;

    @InjectMocks
    private UserService userService;


    @Test
    void shouldLoginSuccessfully() {

        // Arrange

        LoginRequest request = new LoginRequest();
        request.setEmail("divyanshu@gmail.com");
        request.setPassword("123456");

        User user = new User();
        user.setId("1");
        user.setEmail("divyanshu@gmail.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()))
                .thenReturn(true);

        when(jwtService.generateToken(user.getEmail()))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken(user.getEmail()))
                .thenReturn("refresh-token");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        // Act

        LoginResponse response = userService.login(request);

        // Assert

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());

        // Verify

        verify(userRepository).findByEmail(request.getEmail());

        verify(passwordEncoder)
                .matches(request.getPassword(), user.getPassword());

        verify(jwtService)
                .generateToken(user.getEmail());

        verify(jwtService)
                .generateRefreshToken(user.getEmail());

        verify(userRepository)
                .save(user);
    }
}
