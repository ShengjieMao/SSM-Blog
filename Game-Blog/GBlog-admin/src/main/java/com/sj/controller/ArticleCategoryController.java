package com.sj.controller;

import com.alibaba.excel.EasyExcel;
import com.sj.domain.ResponseResult;
import com.sj.domain.dto.CategoryDto;
import com.sj.domain.vo.DownloadDataVo;
import com.sj.service.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import static com.sj.constants.CommonConstants.ARTICLE_CATEGORY;

@RestController
@RequestMapping("/content/category")
public class ArticleCategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult getArticleCategoryList(Integer pageNum, Integer pageSize,
                                                 CategoryDto categoryDto) {
        return categoryService.getArticleCategoryList(pageNum, pageSize, categoryDto);
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getCategoryOneById(@PathVariable Long id){
        return categoryService.getCategoryOneById(id);
    }

    @PutMapping
    public ResponseResult updateCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable Long id){
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/listAllCategory")
    public ResponseResult getNameArticleCategoryList(){
        return categoryService.getNameArticleCategoryList();
    }

    @PreAuthorize("@permission.hasAuthority('content:category:export')")
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response){
        try {
            categoryService.getExcelData(ARTICLE_CATEGORY,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}