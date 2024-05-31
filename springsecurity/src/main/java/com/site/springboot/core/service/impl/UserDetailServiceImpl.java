package com.site.springboot.core.service.impl;

import com.site.springboot.core.entity.AuthenticUser;
import com.site.springboot.core.service.AuthenticUserService;
import com.site.springboot.core.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService {


    @Autowired
    private AuthenticUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthenticUser sysUser = userService.getSysUserByUsername(username);
        sysUser.setAccountNoExpired(1);
        sysUser.setAccountNoLocked(1);
        sysUser.setEnabled(1);
        sysUser.setCredentialsNoExpired(1);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return sysUser;
    }
}
