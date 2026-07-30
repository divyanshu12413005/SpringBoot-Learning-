package com.divyanshu.learnspringsecurityjwt;

import com.divyanshu.learnspringsecurityjwt.dto.*;
import com.divyanshu.learnspringsecurityjwt.entity.Role;
import com.divyanshu.learnspringsecurityjwt.entity.User;
import com.divyanshu.learnspringsecurityjwt.exception.*;
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
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void shouldThrowUserNotFoundExceptionWhenEmailDoesNotExist() {

        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("abc@gmail.com");
        request.setPassword("123456");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.login(request)
        );

        assertEquals("User not found", exception.getMessage());

        // Verify
        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtService, never()).generateToken(any());
    }


    @Test
    void shouldThrowInvalidPasswordExceptionWhenPasswordIsWrong() {

        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("divyanshu@gmail.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("divyanshu@gmail.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()))
                .thenReturn(false);

        // Act & Assert
        InvalidPasswordException exception = assertThrows(
                InvalidPasswordException.class,
                () -> userService.login(request)
        );

        assertEquals("Invalid Password", exception.getMessage());

        // Verify
        verify(userRepository).findByEmail(request.getEmail());

        verify(passwordEncoder)
                .matches(request.getPassword(), user.getPassword());

        verify(jwtService, never()).generateToken(any());
        verify(jwtService, never()).generateRefreshToken(any());

        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void shouldRegisterSuccessfully() {

        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setName("Divyanshu");
        request.setEmail("divyanshu@gmail.com");
        request.setPassword("123456");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserResponse response = userService.register(request);

        // Assert
        assertEquals("Divyanshu", response.getName());
        assertEquals("divyanshu@gmail.com", response.getEmail());

        // Verify
        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).encode(request.getPassword());

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("Divyanshu", savedUser.getName());
        assertEquals("divyanshu@gmail.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(Role.USER, savedUser.getRole());

        // Verify order of method calls
        InOrder inOrder = inOrder(userRepository, passwordEncoder);

        inOrder.verify(userRepository)
                .findByEmail(request.getEmail());

        inOrder.verify(passwordEncoder)
                .encode(request.getPassword());

        inOrder.verify(userRepository)
                .save(any(User.class));
    }


    @Test
    void shouldThrowEmailAlreadyExistsExceptionWhenEmailAlreadyExists() {

        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setName("Divyanshu");
        request.setEmail("divyanshu@gmail.com");
        request.setPassword("123456");

        User existingUser = new User();
        existingUser.setEmail("divyanshu@gmail.com");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(existingUser));

        // Act & Assert
        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.register(request)
        );

        assertEquals("Email already exists", exception.getMessage());

        // Verify
        verify(userRepository).findByEmail(request.getEmail());

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void shouldSendOtpSuccessfully() {

        // Arrange
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("divyanshu@gmail.com");

        User user = new User();
        user.setEmail("divyanshu@gmail.com");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(redisService.increment("otp-limit:" + user.getEmail()))
                .thenReturn(1L);

        when(otpService.generateOtp())
                .thenReturn("123456");

        // Act
        String response = userService.forgotPassword(request);

        // Assert
        assertEquals("OTP sent successfully", response);

        // Verify
        verify(userRepository).findByEmail(request.getEmail());

        verify(redisService)
                .increment("otp-limit:" + user.getEmail());

        verify(redisService)
                .setExpiry(
                        "otp-limit:" + user.getEmail(),
                        10,
                        TimeUnit.MINUTES
                );

        verify(otpService).generateOtp();

        verify(redisService)
                .saveWithExpiry(
                        "otp:" + user.getEmail(),
                        "123456",
                        5
                );

        verify(emailService)
                .sendOtp(user.getEmail(), "123456");
    }


    @Test
    void shouldThrowUserNotFoundExceptionWhenForgotPasswordEmailDoesNotExist() {

        // Arrange
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("abc@gmail.com");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
               UserNotFoundException.class,
                () -> userService.forgotPassword(request)
        );

        assertEquals("User not found", exception.getMessage());

        // Verify
        verify(userRepository).findByEmail(request.getEmail());

        verify(redisService, never()).increment(any());
        verify(otpService, never()).generateOtp();
        verify(emailService, never()).sendOtp(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenOtpRequestLimitExceeded() {

        // Arrange
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("divyanshu@gmail.com");

        User user = new User();
        user.setEmail("divyanshu@gmail.com");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(redisService.increment("otp-limit:" + user.getEmail()))
                .thenReturn(4L);

        // Act & Assert
        OtpRequestLimitExceededException exception = assertThrows(
                OtpRequestLimitExceededException.class,
                () -> userService.forgotPassword(request)
        );

        assertEquals(
                "Too many OTP requests. Please try again after 10 minutes.",
                exception.getMessage()
        );

        // Verify
        verify(userRepository).findByEmail(request.getEmail());

        verify(redisService)
                .increment("otp-limit:" + user.getEmail());

        verify(redisService, never())
                .saveWithExpiry(any(), any(), anyInt());

        verify(otpService, never())
                .generateOtp();

        verify(emailService, never())
                .sendOtp(any(), any());
    }

    @Test
    void shouldResetPasswordSuccessfully() {

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("divyanshu@gmail.com");
        request.setNewPassword("newPassword123");

        User user = new User();
        user.setEmail("divyanshu@gmail.com");
        user.setPassword("oldPassword");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(redisService.get("verified:" + request.getEmail()))
                .thenReturn("true");

        when(passwordEncoder.encode(request.getNewPassword()))
                .thenReturn("encodedPassword");

        String result = userService.resetPassword(request);

        assertEquals("Password Reset Successfully", result);

        assertEquals("encodedPassword", user.getPassword());

        verify(userRepository).save(user);

        verify(redisService).delete("otp:" + request.getEmail());

        verify(redisService).delete("verified:" + request.getEmail());
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenResetPasswordEmailDoesNotExist() {

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("abc@gmail.com");
        request.setNewPassword("newPassword123");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.resetPassword(request)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findByEmail(request.getEmail());

        verify(userRepository, never()).save(any());

        verify(redisService, never()).delete(any());
    }

    @Test
    void shouldThrowOtpNotVerifiedExceptionWhenOtpIsNotVerified() {

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("divyanshu@gmail.com");
        request.setNewPassword("newPassword123");

        User user = new User();
        user.setEmail("divyanshu@gmail.com");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(redisService.get("verified:" + request.getEmail()))
                .thenReturn(null);

        OtpNotVerifiedException exception = assertThrows(
                OtpNotVerifiedException.class,
                () -> userService.resetPassword(request)
        );

        assertEquals("OTP not verified", exception.getMessage());

        verify(userRepository).findByEmail(request.getEmail());

        verify(redisService).get("verified:" + request.getEmail());

        verify(userRepository, never()).save(any());

        verify(redisService, never()).delete(any());

        verify(passwordEncoder, never()).encode(any());
    }
}
