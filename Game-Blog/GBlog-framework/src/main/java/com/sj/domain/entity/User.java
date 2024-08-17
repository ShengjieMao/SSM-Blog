package com.sj.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     *
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 0 - user, 1 - admin
     */
    private String type;

    /**
     * 0 - normal, 1 - terminated
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
     * 0 - M, 1 - F, 2 - Other
     */
    private String sex;

    /**
     *
     */
    private String avatar;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     *
     */
    @JsonFormat(timezone = "GMT-7", pattern = "MM-dd-yyyy HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private String createTime;

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
     * 0 - Normal, 1 - deleted
     */
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}