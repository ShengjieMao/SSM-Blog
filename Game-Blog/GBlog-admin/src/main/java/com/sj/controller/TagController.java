package com.sj.controller;

import com.sj.domain.ResponseResult;
import com.sj.domain.vo.TagByIdVo;
import com.sj.domain.dto.TagDto;
import com.sj.domain.vo.PageVo;
import com.sj.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> getTagList(Integer pageNum, Integer pageSize,
                                             TagDto tagDto){
        return tagService.getTagList(pageNum, pageSize, tagDto);
    }

    @PostMapping()
    public ResponseResult addTag(@RequestBody TagDto tagDto){
        return tagService.addTag(tagDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable Long id){
        return tagService.deleteTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getTagById(@PathVariable Long id){
        return tagService.getTagById(id);
    }

    @PutMapping()
    public ResponseResult updateTag(@RequestBody TagByIdVo tagByIdVo){
        return tagService.updateTag(tagByIdVo);
    }

    @GetMapping("/listAllTag")
    public ResponseResult getNameTagList(){
        return tagService.getNameTagList();
    }
}