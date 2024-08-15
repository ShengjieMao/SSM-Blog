package com.sj.service;

import com.sj.domain.entity.ArticleTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ArticleTagService extends IService<ArticleTag> {
    public List<Long> getTagList(Long id);
}