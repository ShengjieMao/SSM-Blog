package com.sj.service;

import com.sj.domain.ResponseResult;
import com.sj.domain.dto.AddLinkDto;
import com.sj.domain.dto.LinkDto;
import com.sj.domain.dto.LinkStatusDto;
import com.sj.domain.entity.Link;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.vo.LinkVo;

public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult getAllLinkByAdmin(Integer pageNum, Integer pageSize, LinkDto linkDto);

    ResponseResult addLink(AddLinkDto addLinkDto);

    ResponseResult deleteLink(Long id);

    ResponseResult getLinkOneById(Long id);

    ResponseResult updateLink(LinkDto linkDto);

    ResponseResult updateLinkStatus(LinkStatusDto linkStatusDto);
}