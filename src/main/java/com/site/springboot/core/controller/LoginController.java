package com.site.springboot.core.controller;

import cn.hutool.captcha.ShearCaptcha;
import com.site.springboot.core.entity.Admin;
import com.site.springboot.core.service.AdminService;
import com.site.springboot.core.service.JwtService;
import com.site.springboot.core.util.MD5Util;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Resource
    private AuthenticationManager authenticationManager;


    @Resource
    private JwtService jwtService;


    @Resource
    private AdminService adminService;


    @GetMapping("/login")
    public String jumpLogin(Model model) {
        return "login";
    }


    @PostMapping("/token")
    @ResponseBody
    public String login(HttpServletResponse response, HttpServletRequest request, String username, String password, String verifyCode) {
        Authentication token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(token);

        if (username == null || password == null) {
            return "message :username or password is empty";
        }


        if (authenticate.isAuthenticated()) {
            return jwtService.generateToken(username);
        } else {
            throw new UsernameNotFoundException(password);
        }
    }


    @PostMapping("/admin/validate/**")
    @ResponseBody
    public String validate() {
        return "success";
    }


    @PostMapping("/logout")
    @ResponseBody
    public String logout(@RequestBody Admin authRequest) {
        return "success";
    }


}
