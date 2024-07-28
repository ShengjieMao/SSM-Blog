package com.sj.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value ="sj_article")
public class Article implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Title
     */
    private String title;

    /**
     * Content
     */
    private String content;

    /**
     * Summary
     */
    private String summary;

    /**
     * Categoryid
     */
    private Long categoryId;

    /**
     * CategoryName
     */

    @TableField(exist = false)
    private String categoryName;

    /**
     * Thumbnail
     */
    private String thumbnail;

    /**
     * Top the item（0-N，1-Y）
     */
    private String isTop;

    /**
     * Status（0-Sent，1-Draft）
     */
    private String status;

    /**
     * View Count
     */
    private Long viewCount;

    /**
     * Allowing comments (0-N，1-Y)
     */
    private String isComment;

    @TableField(exist = false)
    private List<Long> tags;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * Delete Status（0-Not Deleted，1-Deleted）
     */
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public Article(Long id, Long viewCount) {
        this.id = id;
        this.viewCount = viewCount;
    }
}
