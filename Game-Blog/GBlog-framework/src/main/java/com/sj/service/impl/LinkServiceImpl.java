package com.sj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.ResponseResult;
import com.sj.domain.dto.AddLinkDto;
import com.sj.domain.dto.LinkDto;
import com.sj.domain.dto.LinkStatusDto;
import com.sj.domain.entity.Link;
import com.sj.domain.vo.AdminLinkVo;
import com.sj.domain.vo.LinkVo;
import com.sj.domain.vo.PageVo;
import com.sj.mapper.LinkMapper;
import com.sj.service.LinkService;
import com.sj.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.sj.constants.CommonConstants.*;
import static com.sj.enums.AppHttpCodeEnum.*;

@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link>
        implements LinkService {


    @Resource
    private LinkMapper linkMapper;

    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<Link>();
        queryWrapper.eq(Link::getStatus, LINK_STATUS_NORMAL);
        List<Link> lists = list(queryWrapper);

        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(lists, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult getAllLinkByAdmin(Integer pageNum, Integer pageSize, LinkDto linkDto) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(linkDto.getStatus()), Link::getStatus, linkDto.getStatus());
        queryWrapper.like(StringUtils.hasText(linkDto.getName()), Link::getName, linkDto.getName());

        Page<Link> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);

        List<Link> links = page.getRecords();
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);

        AdminLinkVo adminLinkVo = new AdminLinkVo(linkVos, page.getTotal());

        return ResponseResult.okResult(adminLinkVo);
    }

    @Override
    public ResponseResult addLink(AddLinkDto addLinkDto) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Link::getName, addLinkDto.getName());
        Link link = getOne(queryWrapper);
        if (!Objects.isNull(link)) {
            return ResponseResult.errorResult(LINK_IS_EXIST);
        }

        Link addLink = BeanCopyUtils.copyBean(addLinkDto, Link.class);
        save(addLink);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(Long id) {
        boolean result = removeById(id);
        if (result == false) {
            return ResponseResult.errorResult(DELETE_LINK_FAIL);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLinkOneById(Long id) {
        Link link = getById(id);
        LinkVo linkVo = BeanCopyUtils.copyBean(link, LinkVo.class);

        return ResponseResult.okResult(linkVo);
    }

    @Override
    public ResponseResult updateLink(LinkDto linkDto) {
        if (!StringUtils.hasText(linkDto.getName()) ||
                !StringUtils.hasText(linkDto.getAddress()) ||
                !StringUtils.hasText(String.valueOf(linkDto.getStatus())) ||
                !StringUtils.hasText(linkDto.getLogo()) ||
                !StringUtils.hasText(linkDto.getDescription())) {
            return ResponseResult.errorResult(CONTENT_IS_BLANK);
        }

        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        updateById(link);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateLinkStatus(LinkStatusDto linkStatusDto) {
        UpdateWrapper<Link> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(LINK_ID, linkStatusDto.getId());
        updateWrapper.set(LINK_STATUS, linkStatusDto.getStatus());

        linkMapper.update(null, updateWrapper);

        return ResponseResult.okResult();
    }

    @Override
    public PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.hasText(link.getName()), Link::getName,
                link.getName());
        queryWrapper.eq(Objects.nonNull(link.getStatus()), Link::getStatus,
                link.getStatus());

        Page<Link> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        List<Link> categories = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(categories);

        return pageVo;
    }
}