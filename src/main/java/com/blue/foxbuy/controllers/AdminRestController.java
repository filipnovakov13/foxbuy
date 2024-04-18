package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.BanDTO;
import com.blue.foxbuy.models.DTOs.BanResultDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class AdminRestController {
    private final UserRepository userRepository;
    private final AdminService adminService;

    public AdminRestController(UserRepository userRepository, AdminService adminService) {
        this.userRepository = userRepository;
        this.adminService = adminService;
    }

    @PostMapping("/user/{id}/ban")
    public ResponseEntity<?> ban(@PathVariable UUID id, BanDTO banDTO) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            BanResultDTO banResultDTO = adminService.banUser(banDTO, id);
            return ResponseEntity.ok().body(banResultDTO);
        } else return ResponseEntity.notFound().build();
    }
}
