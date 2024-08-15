package com.sj.service;

import com.sj.domain.ResponseResult;
import com.sj.domain.vo.TagByIdVo;
import com.sj.domain.dto.TagDto;
import com.sj.domain.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.vo.PageVo;
import com.sj.domain.vo.TagByIdVo;
import com.sj.domain.vo.TagVo;

import java.util.List;

/**
*
*/
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> getTagList(Integer pageNum, Integer pageSize, TagDto tagDto);

    ResponseResult addTag(TagDto tagDto);

    ResponseResult deleteTag(Long id);

    ResponseResult getTagById(Long id);

    ResponseResult updateTag(TagByIdVo tagByIdVo);

    ResponseResult getNameTagList();

    List<TagVo> listAllTag();
}