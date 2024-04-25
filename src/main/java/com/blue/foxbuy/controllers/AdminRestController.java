package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.BanDTO;
import com.blue.foxbuy.models.DTOs.BanResultDTO;
import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.AdminService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@Hidden
public class AdminRestController {
    private final UserRepository userRepository;
    private final AdminService adminService;

    public AdminRestController(UserRepository userRepository, AdminService adminService) {
        this.userRepository = userRepository;
        this.adminService = adminService;
    }

    @PostMapping("/user/{id}/ban")
    public ResponseEntity<?> ban(@PathVariable UUID id, @Valid @RequestBody BanDTO banDTO) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            BanResultDTO banResultDTO = adminService.banUser(banDTO, id);
            return ResponseEntity.ok().body(banResultDTO);
        } else {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("404");
            errorDTO.setMessage("User not found.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
        }
    }
}
