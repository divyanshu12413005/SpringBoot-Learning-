package com.divyanshu.learnspringsecurityjwt.service;

import com.divyanshu.learnspringsecurityjwt.entity.Role;
import com.divyanshu.learnspringsecurityjwt.entity.User;
import com.divyanshu.learnspringsecurityjwt.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oauthUser =
                new DefaultOAuth2UserService().loadUser(userRequest);

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        Optional<User> existingUser =
                userRepository.findByEmail(email);

        if (existingUser.isEmpty()) {

            User user = new User();

            user.setName(name);
            user.setEmail(email);
            user.setRole(Role.USER);
            user.setProvider("GOOGLE");
            user.setProfilePicture(picture);
            user.setCreatedAt(LocalDateTime.now());

            userRepository.save(user);

        } else {

            User user = existingUser.get();

            user.setProfilePicture(picture);

            userRepository.save(user);
        }

        return oauthUser;
    }
}