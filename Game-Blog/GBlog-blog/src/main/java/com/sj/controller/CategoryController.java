package com.sj.controller;

import com.sj.domain.ResponseResult;
import com.sj.domain.entity.Category;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.sj.domain.vo.CategoryVo;
import com.sj.domain.vo.DownloadDataVo;
import com.sj.enums.AppHttpCodeEnum;
import com.sj.service.CategoryService;
import com.sj.utils.BeanCopyUtils;
import com.sj.utils.WebUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList() {
        return categoryService.getCategoryList();
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            WebUtils.setDownloadHeader("Category.xlsx", response);
            List<Category> categoryVos = categoryService.list();

            List<DownloadDataVo> downloadCategoryVos = BeanCopyUtils.copyBeanList(categoryVos,
                    DownloadDataVo.class);
            EasyExcel.write(response.getOutputStream(), DownloadDataVo.class)
                    .autoCloseStream(Boolean.FALSE).sheet("Category Exports")
                    .doWrite(downloadCategoryVos);
        } catch (Exception e) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}
