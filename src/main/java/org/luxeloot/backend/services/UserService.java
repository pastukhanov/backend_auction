package org.luxeloot.backend.services;

import org.luxeloot.backend.dto.PassportChangeRequest;
import org.luxeloot.backend.dto.UserRequest;
import org.luxeloot.backend.models.User;
import org.luxeloot.backend.repository.UserRepository;
import org.luxeloot.backend.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUser(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }

    public UserResponse convertToUserResponse(User user) {
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .logo(user.getLogo())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
        return userResponse;
    }

    public UserResponse whoami(String email) {
        return convertToUserResponse(getUser(email));
    }

    public UserResponse updateUser(Long id, UserRequest userDetails, String userName) {
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getId().equals(id)) {
            user.setLogo(userDetails.getLogo());
            user.setFirstname(userDetails.getFirstname());
            user.setLastname(userDetails.getLastname());
            user.setEmail(userDetails.getEmail());
            userRepository.save(user);
        }
        return convertToUserResponse(user);
    }

    public List<UserResponse> listAllUsers() {
        List<UserResponse> users = userRepository.findAll().stream().map(
                user -> {
                    return convertToUserResponse(user);
                }
        ).collect(Collectors.toList());
        return users;
    }

    public UserResponse updateUserPassword(PassportChangeRequest userDetails, String userName) {
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(userDetails.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is not correct");
        }
        user.setPassword(passwordEncoder.encode(userDetails.getNewPassword()));
        User updatedUser = userRepository.save(user);
        return convertToUserResponse(updatedUser);
    }
}
