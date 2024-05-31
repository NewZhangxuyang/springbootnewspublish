package com.site.springboot.core.service;

import com.site.springboot.core.entity.AuthenticUser;

public interface AuthenticUserService {
    AuthenticUser getSysUserByUsername(String username);
}
