package com.site.springboot.core.service.impl;

import com.site.springboot.core.config.Constants;
import com.site.springboot.core.dao.NewsCommentMapper;
import com.site.springboot.core.entity.NewsComment;
import com.site.springboot.core.service.CommentService;
import com.site.springboot.core.util.PageQueryUtil;
import com.site.springboot.core.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private NewsCommentMapper newsCommentMapper;

    @Override
    public Boolean addComment(NewsComment newsComment) {
        newsComment.setCommentStatus(Constants.Check_Status_UnChecked);
        newsComment.setIsDeleted(Constants.Delete_Flag_Exist);
        return newsCommentMapper.save(newsComment).getCommentId() > 0;
    }

    @Override
    public PageResult getCommentsPage(PageQueryUtil pageUtil) {
        PageRequest pageRequest = PageRequest.of(pageUtil.getPage() - 1, pageUtil.getLimit());
        List<NewsComment> comments = newsCommentMapper.findNewsCommentsByIsDeleted(pageRequest, Constants.Delete_Flag_Exist);
        int total = newsCommentMapper.countAllByIsDeleted(Constants.Delete_Flag_Exist);
        return new PageResult(comments, total, pageUtil.getLimit(), pageUtil.getPage());
    }

    @Override
    public Boolean checkDone(Long[] ids) {
        List<NewsComment> commentList = Arrays.stream(ids)
                .map(id -> newsCommentMapper.findNewsCommentByCommentId(id)).peek(
                        newsComment -> newsComment.setCommentStatus(Constants.Check_Status_Checked)
                ).toList();
        return !newsCommentMapper.saveAll(commentList).isEmpty();
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        List<NewsComment> lists = newsCommentMapper.findAllById(Arrays.asList(ids)).stream().peek(newsComment -> {
            newsComment.setIsDeleted(Constants.Delete_Flag_Delete);
        }).toList();
        newsCommentMapper.saveAll(lists);
        //删除分类数据
        return true;
    }
}
