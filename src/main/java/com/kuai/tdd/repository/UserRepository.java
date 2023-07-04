package com.kuai.tdd.repository;

import com.kuai.tdd.dao.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findUserByUsername(String username);

    void increaseUserWrongPassTimes(String login_name);
}
