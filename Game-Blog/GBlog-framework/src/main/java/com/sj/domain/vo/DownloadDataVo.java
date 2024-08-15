package com.sj.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadDataVo {

    @ExcelProperty("categoryId")
    private Long id;

    @ExcelProperty("category")
    private String name;

    @ExcelProperty("status")
    private String status;

    @ExcelProperty("description")
    private String description;
}