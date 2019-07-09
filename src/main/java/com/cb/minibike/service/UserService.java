package com.cb.minibike.service;

import com.cb.minibike.entity.User;

public interface UserService {
    User getUserByOpenId (String openId);

    Boolean genVerifyCode (Integer nationcode, String phoneNum);

    Boolean verify (String phoneNum , String code);

    Boolean register (User user);

    Boolean deposite (User user);

    Boolean identify (User user);

    Boolean recharge (User user);
}
