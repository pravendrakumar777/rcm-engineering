package com.rcm.engineering.service.impl;

import com.rcm.engineering.domain.User;
import com.rcm.engineering.repository.UserRepository;
import com.rcm.engineering.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        log.info("Service Request to save user for credentials: {}", user);
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        log.info("Service Request to findByUsername: {}", username);
        return userRepository.findByUsername(username);
    }
}
