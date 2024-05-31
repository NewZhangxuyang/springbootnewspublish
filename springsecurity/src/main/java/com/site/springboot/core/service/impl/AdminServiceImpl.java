package com.site.springboot.core.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.site.springboot.core.dao.AdminDao;
import com.site.springboot.core.entity.Admin;
import com.site.springboot.core.service.AdminService;
import com.site.springboot.core.util.MD5Util;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private AdminDao adminMapper;


    @Override
    public Admin login(String userName, String password) {
        String passwordMd5 = MD5Util.MD5Encode(password, "UTF-8");
        return adminMapper.findAdminByLoginNameAndLoginPassword(userName, passwordMd5);
    }

    @Override
    public Admin getUserDetailById(Long loginUserId) {
        return adminMapper.findAdminByAdminId(loginUserId);
    }

    @Override
    public Boolean updatePassword(Long loginUserId, String originalPassword, String newPassword) {
        Admin adminUser = adminMapper.findAdminByAdminId(loginUserId);
        //当前用户非空才可以进行更改
        if (adminUser != null) {
            String originalPasswordMd5 = MD5Util.MD5Encode(originalPassword, "UTF-8");
            String newPasswordMd5 = MD5Util.MD5Encode(newPassword, "UTF-8");
            //比较原密码是否正确
            if (originalPasswordMd5.equals(adminUser.getLoginPassword())) {
                //设置新密码并修改
                adminUser.setLoginPassword(newPasswordMd5);
                if (adminMapper.save(adminUser) != null) {
                    //修改成功则返回true
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean updateName(Long loginUserId, String loginUserName, String nickName) {
        Admin adminUser = adminMapper.findAdminByAdminId(loginUserId);
        //当前用户非空才可以进行更改
        if (adminUser != null) {
            //设置新密码并修改
            adminUser.setLoginName(loginUserName);
            adminUser.setAdminNickName(nickName);
            if (adminMapper.save(adminUser) != null) {
                //修改成功则返回true
                return true;
            }
        }
        return false;
    }
}
