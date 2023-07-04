package com.kuai.tdd.utils;

public interface PasswordEncoder {
    boolean match(String encoded_password, String raw_password);
}
