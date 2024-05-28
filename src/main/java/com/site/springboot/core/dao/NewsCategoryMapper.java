package com.site.springboot.core.dao;

import com.site.springboot.core.entity.NewsCategory;
import com.site.springboot.core.entity.NewsComment;
import com.site.springboot.core.util.PageQueryUtil;
import com.site.springboot.core.util.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

public interface NewsCategoryMapper extends JpaRepository<NewsCategory, Long>, QuerydslPredicateExecutor<NewsCategory> {

    int countAllNewsCategoryByIsDeleted(Byte isDeleted);

    NewsCategory findNewsCategoryByCategoryId(Long categoryId);

    Page<NewsCategory> findAllByIsDeleted(Byte isDeleted, Pageable page);

    NewsCategory findNewsCategoryByCategoryName(String categoryName);

    List<NewsCategory> findAllByIsDeleted(Byte isDeleted);

}