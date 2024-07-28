package com.sj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.constants.CommonConstants;
import com.sj.domain.ResponseResult;
import com.sj.domain.entity.Article;
import com.sj.domain.entity.Category;
import com.sj.domain.vo.ArticleDetailVo;
import com.sj.domain.vo.ArticleListVo;
import com.sj.domain.vo.HotArticleVo;
import com.sj.domain.vo.PageVo;
import com.sj.mapper.ArticleMapper;
import com.sj.service.ArticleService;
import com.sj.service.CategoryService;
import com.sj.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult hotArticleList() {
        // Search for the 10 hot articles (blogs) and return as ResponseResult
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // Qualified blogs should not be draft/deleted, sort according to views
        queryWrapper.eq(Article::getStatus, CommonConstants.ARTICLE_STATUS_PUBLISH);
        queryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page = new Page(CommonConstants.CURRENT_PAGE, CommonConstants.SHOW_NUMBERS);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();

        // Bean copy
//        List<HotArticleVo> articleVos = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article, vo);
//            articleVos.add(vo);
//        }
        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(vs);
    }

    @Override
    @Lazy
    public ResponseResult articleList(Integer pageNum, Integer pageSize,
                                      Long categoryId) {
        // categoryId optional
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,
                Article::getCategoryId, categoryId);
        // status should be published, top articles first
        lambdaQueryWrapper.eq(Article::getStatus, CommonConstants.ARTICLE_STATUS_PUBLISH);
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        // Pagination
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);

        List<Article> articles = page.getRecords();
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

//        List<Article> articles = page.getRecords();
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }

        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(),
                ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos, page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetails(Long id) {
        Article article = getById(id);
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article,
                ArticleDetailVo.class);

        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);

        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }

        return ResponseResult.okResult(articleDetailVo);
    }
}

// Remember to install maven project before running the Application