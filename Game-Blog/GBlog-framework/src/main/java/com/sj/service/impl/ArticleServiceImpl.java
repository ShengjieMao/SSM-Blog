package com.sj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.constants.CommonConstants;
import com.sj.domain.ResponseResult;
import com.sj.domain.dto.AddArticleDto;
import com.sj.domain.dto.AdminArticleDto;
import com.sj.domain.dto.ArticleDto;
import com.sj.domain.entity.Article;
import com.sj.domain.entity.ArticleTag;
import com.sj.domain.entity.Category;
import com.sj.domain.vo.*;
import com.sj.mapper.ArticleMapper;
import com.sj.service.ArticleService;
import com.sj.service.ArticleTagService;
import com.sj.service.CategoryService;
import com.sj.utils.BeanCopyUtils;
import com.sj.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sj.constants.CommonConstants.ARTICLE_STATUS_PUBLISH;
import static com.sj.constants.RedisConstants.ARTICLE_VIEWCOUNT;
import static com.sj.enums.AppHttpCodeEnum.DELETE_ARTICLE_FAIL;
//import static sun.security.krb5.Confounder.longValue;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticleList() {
        // Search for the 10 hot articles (blogs) and return as ResponseResult
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // Qualified blogs should not be draft/deleted, sort according to views
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_PUBLISH);
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
        lambdaQueryWrapper.eq(Article::getStatus, ARTICLE_STATUS_PUBLISH);
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

        // Get views from redis
        Integer viewCount = redisCache.getCacheMapValue(ARTICLE_VIEWCOUNT, id.toString());
        article.setViewCount(viewCount.longValue());

        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article,
                ArticleDetailVo.class);

        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);

        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }

        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue(ARTICLE_VIEWCOUNT, id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAllArticleList(Integer pageNum, Integer pageSize, ArticleDto articleDto) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(articleDto.getTitle()), Article::getTitle
                , articleDto.getTitle());
        queryWrapper.like(StringUtils.hasText(articleDto.getSummary()),
                Article::getSummary, articleDto.getSummary());
        queryWrapper.eq(Article::getStatus,ARTICLE_STATUS_PUBLISH);
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();
        List<ArticleDetailsVo> articleDetailsVos = BeanCopyUtils.copyBeanList(articles,
                ArticleDetailsVo.class);
        AdminArticleVo adminArticleVo = new AdminArticleVo(articleDetailsVos, page.getTotal());

        return ResponseResult.okResult(adminArticleVo);
    }

    @Override
    public ResponseResult getArticleById(Long id) {
        Article article = getById(id);
        UpdateArticleVo updateArticleVo = BeanCopyUtils.copyBean(article, UpdateArticleVo.class);

        List<Long> tagList = articleTagService.getTagList(id);
        updateArticleVo.setTags(tagList);

        return ResponseResult.okResult(updateArticleVo);
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        boolean result = removeById(id);
        if (result == false){
            return ResponseResult.errorResult(DELETE_ARTICLE_FAIL);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addArticle(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);

        List<ArticleTag> articleTags = articleDto
                .getTags()
                .stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateArticle(AdminArticleDto adminArticleDto) {
        Article article = BeanCopyUtils.copyBean(adminArticleDto, Article.class);
        List<Long> tagList = articleTagService.getTagList(article.getId());
        List<Long> tags = article.getTags();

        for (Long tag:tags){
            if (!tagList.contains(tag)){
                articleTagService.save(new ArticleTag(article.getId(), tag));
            }
        }
        updateById(article);
        return ResponseResult.okResult();
    }

    @Override
    public PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(article.getTitle()),Article::getTitle, article.getTitle());
        queryWrapper.like(StringUtils.hasText(article.getSummary()),Article::getSummary, article.getSummary());

        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        List<Article> articles = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(articles);
        return pageVo;
    }

    @Override
    public ArticleByIdVo getInfo(Long id) {
        Article article = getById(id);
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        List<ArticleTag> articleTags = articleTagService.list(articleTagLambdaQueryWrapper);
        List<Long> tags = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toList());

        ArticleByIdVo articleVo = BeanCopyUtils.copyBean(article,ArticleByIdVo.class);
        articleVo.setTags(tags);

        return articleVo;
    }

    @Override
    public void edit(ArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        updateById(article);

        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(articleTagLambdaQueryWrapper);

        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(articleDto.getId(), tagId))
                .collect(Collectors.toList());

        articleTagService.saveBatch(articleTags);
    }
}

// Remember to install maven project before running the Application