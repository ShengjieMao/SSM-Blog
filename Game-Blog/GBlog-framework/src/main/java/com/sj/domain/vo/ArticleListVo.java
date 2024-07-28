package com.sj.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListVo {
    private Long id;

    private String title;

    private String summary;

    private String categoryName;

    private String thumbnail;

    private Long viewCount;

    @JsonFormat(pattern = "MM-dd-yyyy HH-mm-ss",timezone = "GMT-7")
    private Date createTime;
}
