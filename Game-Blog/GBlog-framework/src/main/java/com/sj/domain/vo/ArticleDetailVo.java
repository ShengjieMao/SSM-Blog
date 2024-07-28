package com.sj.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVo {
    private Long id;
    private Long categoryId;
    private String categoryName;

    private String content;
    private String thumbnail;
    private String summary;

    @JsonFormat(timezone = "GMT-7", pattern = "MM-dd-yyyy HH:mm:ss")
    private Date createTime;

    private String title;
    private Long viewCount;
}
