package com.site.springboot.core.controller.admin;

import com.site.springboot.core.service.remote.CommentServiceRemote;
import com.site.springboot.core.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import java.util.Map;


@Controller
@RequestMapping("/admin")
public class CommentController {
    @Resource
    private CommentServiceRemote serviceRemote;


    @GetMapping("/comments")
    public String list(HttpServletRequest request) {
        request.setAttribute("path", "comments");
        return "admin/comment";
    }


    @GetMapping("/comments/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        return serviceRemote.list(params);
    }

    @PostMapping("/comments/checkDone")
    @ResponseBody
    public Result checkDone(@RequestBody Long[] ids) {
        return serviceRemote.checkDone(ids);
    }

    @PostMapping("/comments/delete")
    @ResponseBody
    public Result delete(@RequestBody Long[] ids) {
        return serviceRemote.delete(ids);
    }
}
