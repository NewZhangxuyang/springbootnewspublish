package com.site.springboot.core.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {
    @GetMapping("/common/kaptcha")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        ShearCaptcha kaptcha = CaptchaUtil.createShearCaptcha(200, 60, 4, 4);
        request.getSession().setAttribute("verifyCode", kaptcha);
        kaptcha.write(response.getOutputStream());
    }
}
