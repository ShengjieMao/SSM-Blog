package com.sj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.entity.ArticleTag;
import com.sj.mapper.ArticleTagMapper;
import com.sj.service.ArticleTagService;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
        implements ArticleTagService {

    /**
     * 得到文章标签列表
     *
     * @param id id
     * @return {@link List}
     */
    @Test
    public List<Long> getTagList(Long id) {
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, id);
        List<ArticleTag> articleTagList = list(queryWrapper);

        List<Long> tags = articleTagList
                .stream()
                .map(articleTag -> articleTag.getTagId())
                .collect(Collectors.toList());
        return tags;
    }
}