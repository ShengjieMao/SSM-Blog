package com.sj.runner;


import com.sj.domain.entity.Article;
import com.sj.mapper.ArticleMapper;
import com.sj.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sj.constants.RedisConstants.ARTICLE_VIEWCOUNT;

@Component
// Store views to redis when init software with CommandLineRunner
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleMapper articleMapper;

    //---------------------------Stream + function method---------------------------
    //public void run(String... args) throws Exception {
    //    List<Article> articles = articleMapper.selectList(null);
    //    articles.stream()
    //            .collect(Collectors.toMap(new Function<Article, Long>() {
    //                @Override
    //                public Long apply(Article article) {
    //                    return article.getId();
    //                }
    //            }, new Function<Article, Integer>() {
    //                @Override
    //                public Integer apply(Article article) {
    //                    return article.getViewCount().intValue();
    //                }
    //            }));


    @Override
    //------------------- Stream + function + lambda method ------------------------
    public void run(String... args) throws Exception {
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(),
                        article -> article.getViewCount().intValue()));
        // key - id，value - viewCount
        redisCache.setCacheMap(ARTICLE_VIEWCOUNT,viewCountMap);
    }
}
