package com.divyanshu.learnspringsecurityjwt.security;

import com.divyanshu.learnspringsecurityjwt.entity.Role;
import com.divyanshu.learnspringsecurityjwt.entity.User;
import com.divyanshu.learnspringsecurityjwt.repository.UserRepository;
import com.divyanshu.learnspringsecurityjwt.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;


@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {


    private static final Logger logger =
            LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public OAuth2SuccessHandler(UserRepository userRepository,
                                JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String name = oauthUser.getAttribute("name");
        String email = oauthUser.getAttribute("email");
        String picture = oauthUser.getAttribute("picture");

        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user;

        if (optionalUser.isPresent()) {

            user = optionalUser.get();
            user.setName(name);
            user.setProfilePicture(picture);
            user.setProvider("GOOGLE");

        } else {

            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setProvider("GOOGLE");
            user.setProfilePicture(picture);
            user.setRole(Role.USER);
        }


        String accessToken = jwtService.generateToken(email);
        String refreshToken = jwtService.generateRefreshToken(email);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        logger.info("===== GOOGLE LOGIN SUCCESS =====");
        logger.info("Name : {}", name);
        logger.info("Email : {}", email);
        logger.info("Picture : {}", picture);
        logger.info("Saved User : {}", user.getEmail());

        response.setContentType("application/json");

        response.getWriter().write("""
{
    "accessToken":"%s",
    "refreshToken":"%s",
    "email":"%s",
    "name":"%s"
}
""".formatted(
                accessToken,
                refreshToken,
                email,
                name
        ));
    }
}