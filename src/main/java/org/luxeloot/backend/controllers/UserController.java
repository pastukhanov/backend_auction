package org.luxeloot.backend.controllers;


import org.luxeloot.backend.config.JwtService;
import org.luxeloot.backend.dto.PassportChangeRequest;
import org.luxeloot.backend.dto.UserRequest;
import org.luxeloot.backend.services.UserService;
import org.luxeloot.backend.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @GetMapping("/whoami")
    public ResponseEntity<UserResponse> whoAmI(@RequestHeader("Authorization") String token) {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        System.out.println(userName);
        return ResponseEntity.ok(userService.whoami(userName));
    }

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> listAllUsers(@RequestHeader("Authorization") String token) {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        System.out.println(userName);
        return userService.listAllUsers();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                 @RequestBody UserRequest userDetails,
                                                 @RequestHeader("Authorization") String token) {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        return ResponseEntity.ok(userService.updateUser(id, userDetails, userName));
    }


    @PostMapping("/change-password")
    public ResponseEntity<UserResponse> updateUserPassword(@RequestBody PassportChangeRequest userDetails,
                                                   @RequestHeader("Authorization") String token) {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        return ResponseEntity.ok(userService.updateUserPassword(userDetails, userName));
    }
}
