package com.sj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.ResponseResult;
import com.sj.domain.vo.TagByIdVo;
import com.sj.domain.dto.TagDto;
import com.sj.domain.entity.Category;
import com.sj.domain.entity.Tag;
import com.sj.domain.vo.CategoryVo;
import com.sj.domain.vo.PageVo;
import com.sj.domain.vo.TagVo;
import com.sj.enums.AppHttpCodeEnum;
import com.sj.mapper.TagMapper;
import com.sj.service.TagService;
import com.sj.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.sj.constants.CommonConstants.CATEGORY_STATUS_NORMAL;
import static com.sj.enums.AppHttpCodeEnum.CONTENT_IS_BLANK;

/**
 *
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements TagService {

    @Resource
    private TagMapper tagMapper;

    /**
     *
     */
    @Override
    public ResponseResult<PageVo> getTagList(Integer pageNum, Integer pageSize, TagDto tagDto) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagDto.getName()),Tag::getName, tagDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagDto.getRemark()),Tag::getRemark, tagDto.getRemark());

        Page<Tag> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(page.getRecords(), TagVo.class);

        PageVo pageVo = new PageVo(tagVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(TagDto tagDto) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Tag::getName,tagDto.getName());

        Tag getTag = getOne(queryWrapper);
        if (getTag != null){
            return ResponseResult.errorResult(AppHttpCodeEnum.TAG_IS_EXIST);
        }

        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);

        save(tag);
        return ResponseResult.okResult();
    }

   @Override
    public ResponseResult deleteTag(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTagById(Long id) {
        Tag tag = getById(id);
        if (Objects.isNull(tag)){
            return ResponseResult.errorResult(AppHttpCodeEnum.TAG_IS_NOEXIST);
        }

        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(TagByIdVo tagByIdVo) {
        if (!StringUtils.hasText(tagByIdVo.getName()) && !StringUtils.hasText(tagByIdVo.getRemark())){
            return ResponseResult.errorResult(CONTENT_IS_BLANK);
        }
        Tag tag = BeanCopyUtils.copyBean(tagByIdVo, Tag.class);

        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getNameTagList() {
        List<Tag> tags = tagMapper.selectList(null);

        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tags, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }

    @Override
    public List<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId, Tag::getName);
        List<Tag> list = list(wrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return tagVos;
    }
}