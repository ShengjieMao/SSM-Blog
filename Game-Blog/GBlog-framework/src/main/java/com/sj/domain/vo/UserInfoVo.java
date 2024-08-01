package com.sj.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo {
    private Long id;
    private String userName;
    private String nickName;
    private String avatar;
    private String sex;
    private String email;
    private String phonenumber;
    private String status;
    private String createTime;
}