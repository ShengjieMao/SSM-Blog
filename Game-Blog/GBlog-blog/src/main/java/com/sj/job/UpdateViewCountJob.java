package com.sj.job;

import com.sj.domain.entity.Article;
import com.sj.service.ArticleService;
import com.sj.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sj.constants.RedisConstants.ARTICLE_VIEWCOUNT;

@Component
public class UpdateViewCountJob {

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleService articleService;

    /**
     * Update view counts every 10 mins
     * Can edit later - CRON express generator
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateViewCount() {
        Map<String, Integer> articles = redisCache.getCacheMap(ARTICLE_VIEWCOUNT);

        List<Article> articleList = articles.entrySet()
                .stream()
                .map(article -> new Article(Long.valueOf(article.getKey()),
                        Long.valueOf(article.getValue())))
                .collect(Collectors.toList());

        articleService.updateBatchById(articleList);
    }
}