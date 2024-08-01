package com.sj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.ResponseResult;
import com.sj.domain.entity.Comment;

public interface CommentService extends IService<Comment> {

    ResponseResult getCommentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}