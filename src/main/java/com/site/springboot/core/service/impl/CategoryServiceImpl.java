package com.site.springboot.core.service.impl;

import com.site.springboot.core.config.Constants;
import com.site.springboot.core.dao.NewsCategoryMapper;
import com.site.springboot.core.entity.NewsCategory;
import com.site.springboot.core.service.CategoryService;
import com.site.springboot.core.util.PageQueryUtil;
import com.site.springboot.core.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private NewsCategoryMapper newsCategoryMapper;

    @Override
    public List<NewsCategory> getAllCategories() {
        return newsCategoryMapper.findAllByIsDeleted(Constants.Delete_Flag_Exist);
    }

    @Override
    public NewsCategory queryById(Long id) {
        return newsCategoryMapper.findNewsCategoryByCategoryId(id);
    }

    @Override
    public PageResult getCategoryPage(PageQueryUtil pageUtil) {
        PageRequest pageResult = PageRequest.of(pageUtil.getPage() - 1, pageUtil.getLimit());
        List<NewsCategory> categoryList = newsCategoryMapper.findAllByIsDeleted(Constants.Delete_Flag_Exist, pageResult).getContent();
        int total = newsCategoryMapper.countAllNewsCategoryByIsDeleted(Constants.Delete_Flag_Exist);
        return new PageResult(categoryList, total, pageUtil.getLimit(), pageUtil.getPage());
    }

    @Override
    public Boolean saveCategory(String categoryName) {
        NewsCategory temp = newsCategoryMapper.findNewsCategoryByCategoryName(categoryName);
        if (temp == null) {
            NewsCategory newsCategory = new NewsCategory();
            newsCategory.setCategoryName(categoryName);
            newsCategory.setIsDeleted(Constants.Delete_Flag_Exist);
            return !Objects.isNull(newsCategoryMapper.save(newsCategory));
        }
        return false;
    }

    @Override
    public Boolean updateCategory(Long categoryId, String categoryName) {
        NewsCategory newsCategory = newsCategoryMapper.findNewsCategoryByCategoryId(categoryId);
        if (newsCategory != null) {
            newsCategory.setCategoryName(categoryName);
            return !Objects.isNull(newsCategoryMapper.save(newsCategory));
        }
        return false;
    }

    @Override
    public Boolean deleteBatchByIds(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        List<NewsCategory> lists = newsCategoryMapper.findAllById(Arrays.asList(ids)).stream().peek(newsCategory -> {
            newsCategory.setIsDeleted(Constants.Delete_Flag_Delete);
        }).toList();
        newsCategoryMapper.saveAll(lists);
        return true;
    }

}
