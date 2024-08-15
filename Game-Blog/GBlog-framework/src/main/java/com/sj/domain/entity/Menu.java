package com.sj.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@TableName(value ="sys_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Menu implements Serializable {
    @TableId
    private Long id;

    private String menuName;
    private Long parentId;
    private Integer orderNum;

    private String path;
    private String component;

    private Integer isFrame;
    private String menuType;
    private String visible;
    private String status;

    private String perms;
    private String icon;

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private String remark;

    private String delFlag;

    @TableField(exist = false)
    private List<Menu> children;
}
