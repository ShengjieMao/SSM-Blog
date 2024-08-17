package com.sj.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleByIdVo {

    private Long id;
    private String title;
    private String content;
    private String summary;
    private Long categoryId;

    private String thumbnail;
    private String isTop;
    private String status;
    private Long viewCount;
    // Allowance of comments: 1 - Y, 0 - N
    private String isComment;
    private List<Long> tags;
}