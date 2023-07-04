package com.kuai.tdd.service.impl;

import com.kuai.tdd.dao.User;
import com.kuai.tdd.repository.UserRepository;
import com.kuai.tdd.service.UserService;
import com.kuai.tdd.utils.PasswordEncoder;
import lombok.Setter;

@Setter
public class UserServiceImpl implements UserService {
    public static final String WRONG_PASS = "Wrong pass";
    public static final String USER_NOT_FOUND = "User not found";
    public static final int MAX_TRY = 5;
    public static final String EXCEED_MAX_TRY = "Exceed max try";
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public User loginWithUsernameAndPassword(String username, String rawPassword) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

        if(user.getErrorTimes() >= MAX_TRY) {
            throw new RuntimeException(EXCEED_MAX_TRY);
        }

        if(checkUserPassword(user, rawPassword)) {
            return user;
        } else {
            userRepository.increaseUserWrongPassTimes(username);
            throw new RuntimeException(WRONG_PASS);
        }
    }

    private boolean checkUserPassword(User user, String rawPassword) {
        return passwordEncoder.match(user.getPassword(), rawPassword);
    }
}
