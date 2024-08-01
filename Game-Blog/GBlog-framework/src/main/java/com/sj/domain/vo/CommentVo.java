package com.sj.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private Long articleId;

    /**
     *
     */
    private Long rootId;

    /**
     *
     */
    private String username;

    /**
     *
     */
    private String content;

    /**
     *
     */
    private Long toCommentUserId;

    /**
     *
     */
    private Long toCommentId;

    /**
     *
     */
    private String toCommentUserName;

    /**
     *
     */
    private List<CommentVo> children;

    /**
     *
     */
    private Long createBy;

    /**
     *
     */
    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss", timezone = "GMT-7")
    private Date createTime;


}
