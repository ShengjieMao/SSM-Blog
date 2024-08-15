package com.sj.controller;

import com.sj.domain.ResponseResult;
import com.sj.domain.dto.AddArticleDto;
import com.sj.domain.dto.AdminArticleDto;
import com.sj.domain.dto.ArticleDto;
import com.sj.domain.vo.ArticleDetailsVo;
import com.sj.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @GetMapping("/list")
    public ResponseResult getAllArticleList(Integer pageNum, Integer pageSize, ArticleDto articleDto) {
        return articleService.getAllArticleList(pageNum, pageSize, articleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable Long id){
        return articleService.getArticleById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable Long id){
        return articleService.deleteArticle(id);
    }

    @PostMapping
    public ResponseResult addArticle(@RequestBody AddArticleDto articleDto){
        return articleService.addArticle(articleDto);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody AdminArticleDto adminArticleDto){
        return articleService.updateArticle(adminArticleDto);
    }
}