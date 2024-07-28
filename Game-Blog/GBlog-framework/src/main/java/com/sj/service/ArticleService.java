package com.sj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.ResponseResult;
import com.sj.domain.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize,
                               Long categoryId);

    ResponseResult getArticleDetails(Long id);
}
