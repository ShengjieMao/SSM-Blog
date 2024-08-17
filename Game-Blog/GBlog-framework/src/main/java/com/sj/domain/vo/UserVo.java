package com.sj.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class UserVo {
    /**
     *
     */
    private Long id;
    /**
     *
     */
    private String userName;
    /**
     *
     */
    private String nickName;

    /**
     *
     */
    private String status;
    /**
     *
     */
    private String email;
    /**
     *
     */
    private String phonenumber;
    /**
     *
     */
    private String sex;
    /**
     *
     */
    private String avatar;

    /**
     *
     */
    private Long createBy;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Long updateBy;
    /**
     *
     */
    private Date updateTime;

}