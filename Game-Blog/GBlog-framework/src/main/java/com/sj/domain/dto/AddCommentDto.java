package com.sj.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "AddCommentDto")
public class AddCommentDto {

    private Long id;

    /**
     *
     */
    @ApiModelProperty(notes = "Comment Type: 0 - blog comment, 1 - Link")
    private String type;

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
    private String content;

    /**
     *
     */
    private Long toCommentUserId;

    /**
     *
     */
    private Long toCommentId;
}