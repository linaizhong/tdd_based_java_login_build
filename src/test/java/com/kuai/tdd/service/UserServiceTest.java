package com.kuai.tdd.service;

import com.kuai.tdd.dao.User;
import com.kuai.tdd.repository.UserRepository;
import com.kuai.tdd.service.impl.UserServiceImpl;
import com.kuai.tdd.utils.PasswordEncoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService = new UserServiceImpl();
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void userLoginPass() {
        when(userRepository.findUserByUsername("login_name")).thenReturn(
                Optional.ofNullable(
                User.builder()
                .id("user_id")
                .username("user")
                .password("encoded_password")
                .build())
        );

        when(passwordEncoder.match("encoded_password", "123456")).thenReturn(true);

        User user = userService.loginWithUsernameAndPassword("login_name", "123456");

        assertEquals(user.getId(), "user_id");
        assertEquals(user.getUsername(), "user");
    }

    @Test
    public void userLoginFail() {
        when(userRepository.findUserByUsername("login_name")).thenReturn(
                Optional.ofNullable(
                        User.builder()
                                .id("user_id")
                                .username("user")
                                .password("encoded_password")
                                .build())
        );

        when(passwordEncoder.match("encoded_password", "123456")).thenReturn(false);

        try {
            User user = userService.loginWithUsernameAndPassword("login_name", "123456");
            throw new RuntimeException("Should not come here!!!");
        } catch(RuntimeException e) {
            assertEquals(e.getMessage(), UserServiceImpl.WRONG_PASS);
            verify(userRepository).increaseUserWrongPassTimes("login_name");
        }
    }

    @Test
    public void usernameNotFound() {
        when(userRepository.findUserByUsername("login_name")).thenReturn(Optional.empty());

        try {
            User user = userService.loginWithUsernameAndPassword("login_name", "123456");
            throw new RuntimeException("Should not come here!!!");
        } catch(RuntimeException e) {
            assertEquals(e.getMessage(), UserServiceImpl.USER_NOT_FOUND);
        }
    }

    @Test
    public void userLoginExceedMaxTry() {
        when(userRepository.findUserByUsername("login_name")).thenReturn(
                Optional.ofNullable(
                        User.builder()
                                .id("user_id")
                                .username("user")
                                .password("encoded_password")
                                .errorTimes(UserServiceImpl.MAX_TRY)
                                .build())
        );

        try {
            User user = userService.loginWithUsernameAndPassword("login_name", "123456");
            throw new RuntimeException("Should not come here!!!");
        } catch(RuntimeException e) {
            assertEquals(e.getMessage(), UserServiceImpl.EXCEED_MAX_TRY);
        }
    }
}
