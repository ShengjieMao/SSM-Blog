package com.sj.controller;

import com.sj.domain.ResponseResult;
import com.sj.domain.dto.AddCommentDto;
import com.sj.domain.entity.Comment;
import com.sj.service.CommentService;
import com.sj.utils.BeanCopyUtils;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.sj.constants.CommonConstants.ARTICLE_COMMENT;
import static com.sj.constants.CommonConstants.LINK_COMMENT;

@RestController
@RequestMapping("/comment")
@Api(tags = "Comments", description = "Comment ports") // Swagger
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     *
     */
    @GetMapping("/commentList")
    @ApiOperation(value = "SearchComment", notes = "Get article Comments")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId",value = "ArticleId"),
            @ApiImplicitParam(name = "pageNum",value = "Current"),
            @ApiImplicitParam(name = "pageSize",value = "NumPerPage")
        }
    )
    public ResponseResult getCommentList(Long articleId, Integer pageNum, Integer pageSize) {
        return commentService.getCommentList(ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }

    /**
     *
     */
    // Use DTO instead of entity to make processing more regulated
    @PostMapping("/addComment")
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto) {
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }

    /**
     * Links comments
     */
    @GetMapping("/linkCommentList")
    @ApiOperation(value = "Link comment", notes = "Get a page of link comments")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "Page Number"),
            @ApiImplicitParam(name = "pageSize", value = "Count on each page")
    })
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize) {
        return commentService.getCommentList(LINK_COMMENT, null, pageNum, pageSize);
    }
}