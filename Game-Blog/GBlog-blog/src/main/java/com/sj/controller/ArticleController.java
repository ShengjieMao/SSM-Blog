package com.sj.controller;

import com.sj.domain.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.ResponseResult;
import com.sj.service.ArticleService;
import com.sj.utils.RedisCache;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import static com.sj.constants.RedisConstants.ARTICLE_VIEWCOUNT;


@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
//    @Resource
    private ArticleService articleService;

    /**
     * Get the hottest blogs
     */
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList() {
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    /**
     * Get the blog list
     */
    @GetMapping("/articleList")
    public ResponseResult articleList(Long categoryId, Integer pageNum, Integer pageSize){
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    /**
     * Get the details page of the blog
     */
    @GetMapping("/{id}")
    public ResponseResult getArticleDetails(@PathVariable("id") Long id ){
        return articleService.getArticleDetails(id);
    }

    /**
     * Update the total views count for the blog
     */
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable Long id){
        return articleService.updateViewCount(id);
    }
}
