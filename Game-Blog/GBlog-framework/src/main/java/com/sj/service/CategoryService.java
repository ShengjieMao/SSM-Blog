package com.sj.service;

import com.sj.domain.ResponseResult;
import com.sj.domain.dto.CategoryDto;
import com.sj.domain.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.vo.CategoryVo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult getArticleCategoryList(Integer pageNum, Integer pageSize, CategoryDto categoryDto);

    ResponseResult addCategory(CategoryDto categoryDto);

    ResponseResult getCategoryOneById(Long id);

    ResponseResult updateCategory(CategoryDto categoryDto);

    ResponseResult deleteCategory(Long id);

    ResponseResult getNameArticleCategoryList();

    void getExcelData(String filename, HttpServletResponse response) throws IOException;

    String getCategoryName(Long id);

    List<CategoryVo> listAllCategory();
}