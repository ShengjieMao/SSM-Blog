package com.sj.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailsVo {

    private Long id;

    private String categoryId;

    private String categoryName;

    private String content;

    private String thumbnail;

    private String summary;

    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss",timezone = "GMT-7")
    private Date createTime;

    private String title;

    private Long viewCount;




}