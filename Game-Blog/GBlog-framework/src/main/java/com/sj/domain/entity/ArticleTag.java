package com.sj.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="sj_article_tag")
public class ArticleTag implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long articleId;

    private Long tagId;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}