package com.site.springboot.core.service.impl;

import com.site.springboot.core.dao.AdminDao;
import com.site.springboot.core.entity.Admin;
import com.site.springboot.core.entity.AuthenticUser;
import com.site.springboot.core.service.AuthenticUserService;
import com.site.springboot.core.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthenticUserServiceImpl implements AuthenticUserService {

    @Autowired
    private AdminDao userDao;


    @Override
    public AuthenticUser getSysUserByUsername(String username) {
        Admin sysUser = userDao.findAdminByLoginName(username);
        AuthenticUser authenticUser = new AuthenticUser();
        authenticUser.setUsername(sysUser.getLoginName());
        authenticUser.setPassword(sysUser.getLoginPassword());
        return authenticUser;
    }
}
