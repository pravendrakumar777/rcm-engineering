package com.rcm.engineering.service;

import com.rcm.engineering.domain.User;

public interface UserService {

    void save(User user);
    User findByUsername(String username);

}
