package com.kuai.tdd.service;

import com.kuai.tdd.dao.User;

public interface UserService {
    User loginWithUsernameAndPassword(String username, String rawPassword);
}
