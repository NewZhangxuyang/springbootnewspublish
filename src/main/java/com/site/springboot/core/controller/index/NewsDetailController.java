package com.site.springboot.core.controller.index;

import cn.hutool.captcha.ShearCaptcha;
import com.alibaba.fastjson.JSON;
import com.site.springboot.core.entity.News;
import com.site.springboot.core.service.remote.CommentServiceRemote;
import com.site.springboot.core.service.NewsIndexService;
import com.site.springboot.core.service.NewsService;
import com.site.springboot.core.util.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/admin/detail")
public class NewsDetailController {

    @Resource
    private CommentServiceRemote serviceRemote;

    @Resource
    private NewsService newsService;

    @Resource
    private ExcelUtil excelUtil;


    @Resource
    private NewsIndexService indexService;

    /**
     * 详情页
     *
     * @return
     */
    @GetMapping({"/{newsId}"})
    public String detail(HttpServletRequest request, @PathVariable("newsId") Long newsId) {
        News newsDetail = newsService.queryNewsById(newsId);
        if (newsDetail != null) {
            request.setAttribute("newsDetail", newsDetail);
            request.getSession().setAttribute("loginUserId", "zhangxuyang");
            int isPraise = newsService.selectIsPraise(newsId, request.getSession().getAttribute("loginUserId").toString());
            request.setAttribute("praise", isPraise);
            request.setAttribute("pageName", "详情");
            newsService.addViews(newsId);
            return "index/detail";
        } else {
            return "error/error_404";
        }
    }


    @GetMapping("/news/search")
    @ResponseBody
    public Result searchNews(@RequestParam String keyword, @RequestParam Map<String, Object> params) {
        if (ObjectUtils.isEmpty(params.get("page")) || ObjectUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(indexService.getNewsIndexByContent(keyword, pageUtil));
    }


    @PostMapping("/news/export")
    @ResponseBody
    public Result exportNews(@RequestBody Long[] ids, HttpServletResponse response) {
        List<News> news = newsService.getNewsByIds(ids);
        excelUtil.newsExport(news, response);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/news/praise")
    @ResponseBody
    public Result praiseNews(@RequestBody String data, HttpServletRequest request) {
        String user = request.getSession().getAttribute("loginUserId").toString();
        Map map = JSON.parseObject(data, Map.class);
        String newsId = map.get("newsId").toString();
        String praised = map.get("praised").toString();
        if (Long.parseLong(newsId) < 0 && user == null) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (Integer.parseInt(praised) == 1) {
            newsService.UnPraiseNews(Long.parseLong(newsId), user);
            return ResultGenerator.genSuccessResult("取消点赞成功");
        }
        newsService.praiseNews(Long.parseLong(newsId), user);
        return ResultGenerator.genSuccessResult("点赞成功");
    }

    @PostMapping("/news/totalPraise")
    @ResponseBody
    public List<String> getTotalPraiseUser(@RequestBody String data) {
        return newsService.getTotalPraiseUser(Long.parseLong(data));
    }

    @PostMapping("/news/total")
    @ResponseBody
    public int getTotalPraise(@RequestBody String data) {
        return newsService.getTotalPraiseCount(Long.parseLong(data));
    }
    /**
     * 评论操作
     */
    @PostMapping(value = "/news/comment")
    @ResponseBody
    public Result detailComment(HttpServletRequest request, HttpSession session, @RequestParam Long newsId, @RequestParam String verifyCode, @RequestParam String commentator, @RequestParam String commentBody) {
        if (!StringUtils.hasText(verifyCode)) {
            return ResultGenerator.genFailResult("验证码不能为空");
        }
        ShearCaptcha shearCaptcha = (ShearCaptcha) session.getAttribute("verifyCode");
        if (shearCaptcha == null || !shearCaptcha.verify(verifyCode)) {
            return ResultGenerator.genFailResult("验证码错误");
        }
        String ref = request.getHeader("Referer");
        if (!StringUtils.hasText(ref)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (null == newsId || newsId < 0) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (!StringUtils.hasText(commentator)) {
            return ResultGenerator.genFailResult("请输入称呼");
        }
        if (!StringUtils.hasText(commentBody)) {
            return ResultGenerator.genFailResult("请输入评论内容");
        }
        if (commentBody.trim().length() > 200) {
            return ResultGenerator.genFailResult("评论内容过长");
        }
        session.removeAttribute("verifyCode");//留言成功后删除session中的验证码信
        return serviceRemote.comment(newsId, commentator, commentBody);
    }
}
