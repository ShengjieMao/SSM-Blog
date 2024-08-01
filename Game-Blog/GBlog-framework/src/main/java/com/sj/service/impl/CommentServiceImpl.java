package com.sj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.ResponseResult;
import com.sj.domain.entity.Comment;
import com.sj.domain.vo.CommentVo;
import com.sj.domain.vo.PageVo;
import com.sj.enums.AppHttpCodeEnum;
import com.sj.exception.SystemException;
import com.sj.mapper.CommentMapper;
import com.sj.service.CommentService;
import com.sj.service.UserService;
import com.sj.utils.BeanCopyUtils;
import com.sj.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.sj.constants.CommonConstants.ARTICLE_COMMENT;
import static com.sj.constants.CommonConstants.ROOT_ID;
import static com.sj.enums.AppHttpCodeEnum.COMMENT_NOT_NULL;

/**
 *
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Resource
    private UserService userService;

    /**
     *
     */
    @Override
    public ResponseResult getCommentList(String commentType, Long articleId,
                                         Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ARTICLE_COMMENT.equals(commentType), Comment::getArticleId,
                articleId);
        queryWrapper.eq(Comment::getRootId, ROOT_ID); // Check if comment is a root one
        queryWrapper.eq(Comment::getType, commentType);

        Page<Comment> page = new Page(pageNum, pageSize);
        page(page, queryWrapper);

        List<CommentVo> commentVoLists = toCommentListVo(page.getRecords());

        // Manual copy
        commentVoLists.stream().map(
                commentVo -> {
                    List<CommentVo> children = getChildren(commentVo.getId());
                    commentVo.setChildren(children);
                    return commentVo;
                }
        ).collect(Collectors.toList());

        return ResponseResult.okResult(new PageVo(commentVoLists, page.getTotal()));
    }

    /**
     *
     */
    public ResponseResult addComment(Comment comment) {
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(COMMENT_NOT_NULL);
        }
        // comment.setCreateBy((SecurityUtils.getUserId()));
        save(comment);
        return ResponseResult.okResult();
    }


    /**
     *
     */
    private List<CommentVo> toCommentListVo(List<Comment> list) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        commentVos.stream().map(commentVo -> {
            // Get users nickname (display username) from createBy
            String nickname = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickname);

            if (commentVo.getToCommentUserId() != -1) {
                String commentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(commentUserName);
            }
            return commentVo;
        }).collect(Collectors.toList());
        return commentVos;
    }

    /**
     *
     */
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id);

        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> lists = list(queryWrapper);
        return toCommentListVo(lists);
    }
}