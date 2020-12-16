package com.megetood.mrpc.demo.provider;

import com.megetood.mrpc.demo.api.User;
import com.megetood.mrpc.demo.api.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
