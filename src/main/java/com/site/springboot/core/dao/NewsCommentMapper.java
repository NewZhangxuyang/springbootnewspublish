package com.site.springboot.core.dao;

import com.site.springboot.core.entity.News;
import com.site.springboot.core.entity.NewsComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

public interface NewsCommentMapper extends JpaRepository<NewsComment, Long>, QuerydslPredicateExecutor<NewsComment> {

    NewsComment findNewsCommentByCommentId(Long id);

    List<NewsComment> findNewsCommentsByIsDeleted(Pageable page, Byte isDeleted);

    int countAllByIsDeleted(Byte isDeleted);


}