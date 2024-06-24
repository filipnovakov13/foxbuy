package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.DTOs.ListOfUserDetailsDTO;
import com.blue.foxbuy.models.DTOs.UserDetailsDTO;
import com.blue.foxbuy.models.DTOs.UserSummaryDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.implementations.UserServiceImp;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Tag(name = "User details")
public class UserDetailsController {

    private final UserRepository userRepository;

    private final UserServiceImp userServiceImp;

    @Autowired
    public UserDetailsController(UserRepository userRepository, UserServiceImp userServiceImp) {
        this.userRepository = userRepository;
        this.userServiceImp = userServiceImp;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable final UUID id) {
        Optional<UserDetailsDTO> userDetails = userServiceImp.findById(id);
        if (userDetails.isPresent()) {
            return ResponseEntity.ok(userDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorDTO("404", "User with this Id doesn't exist"));
        }
    }

    @GetMapping
    public ResponseEntity<?> findAllUsers(
            @RequestParam (name = "page", defaultValue = "0", required = false) final Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<User> usersPage = userRepository.findAll(pageable);
        if (page >= usersPage.getTotalPages()) {
            return ResponseEntity.badRequest()
                                 .body(new ErrorDTO("400" , "You requested page " + page +
                                                                           " but there are only " +
                                                                           usersPage.getTotalPages() +
                                                                           " pages"));
        }

        List<UserSummaryDTO> userDTOs = usersPage.getContent().stream()
                .map(user -> new UserSummaryDTO(user.getUsername(),
                                                user.getEmail(),
                                                user.getRole(),
                                                user.getAds().size()))
                .toList();

        ListOfUserDetailsDTO response = new ListOfUserDetailsDTO(
                usersPage.getNumber() + 1,
                usersPage.getTotalPages(),
                userDTOs
        );

        return ResponseEntity.ok(response);
    }
}
