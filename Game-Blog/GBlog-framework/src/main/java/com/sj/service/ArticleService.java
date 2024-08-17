package com.sj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.ResponseResult;
import com.sj.domain.dto.AddArticleDto;
import com.sj.domain.dto.AdminArticleDto;
import com.sj.domain.dto.ArticleDto;
import com.sj.domain.entity.Article;
import com.sj.domain.vo.ArticleByIdVo;
import com.sj.domain.vo.PageVo;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize,
                               Long categoryId);

    ResponseResult getArticleDetails(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult getAllArticleList(Integer pageNum, Integer pageSize, ArticleDto articleDto);

    ResponseResult getArticleById(Long id);

    ResponseResult deleteArticle(Long id);

    ResponseResult addArticle(AddArticleDto articleDto);

    ResponseResult updateArticle(AdminArticleDto adminArticleDto);

    // Search pagination
    PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize);

    // Get article based on article id,
    ArticleByIdVo getInfo(Long id);
    // then Edit article
    void edit(ArticleDto article);
}
