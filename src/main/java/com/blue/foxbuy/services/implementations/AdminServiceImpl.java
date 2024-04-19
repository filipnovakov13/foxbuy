package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.models.DTOs.BanDTO;
import com.blue.foxbuy.models.DTOs.BanResultDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public BanResultDTO banUser(BanDTO banDTO, UUID id) {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_MONTH, 5);

        Date banDuration = calendar.getTime();

        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.get();

        user.setBanned(true);
        user.setBanDate(banDuration);

        userRepository.save(user);

        return new BanResultDTO(user.getUsername(), banDuration);
    }
}
