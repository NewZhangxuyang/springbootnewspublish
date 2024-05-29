package com.site.springboot.core.service.remote;


import com.site.springboot.core.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "SpringNewsCommentService", contextId = "CommentServiceClient")
public interface CommentServiceRemote {


    @Retryable(retryFor = Exception.class, maxAttempts = 5)
    @GetMapping("/admin/comments/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params);

    @Retryable(retryFor = Exception.class, maxAttempts = 5)
    @PostMapping("/admin/comments/checkDone")
    @ResponseBody
    public Result checkDone(@RequestBody Long[] ids);

    @Retryable(retryFor = Exception.class, maxAttempts = 5)
    @PostMapping("/admin/comments/delete")
    @ResponseBody
    public Result delete(@RequestBody Long[] ids);

    @Retryable(retryFor = Exception.class, maxAttempts = 5)
    @PostMapping(value = "/admin/news/comment")
    @ResponseBody
    public Result comment(@RequestParam Long newsId, @RequestParam String commentator, @RequestParam String commentBody);
}
