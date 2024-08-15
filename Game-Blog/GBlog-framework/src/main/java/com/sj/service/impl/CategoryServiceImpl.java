package com.sj.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.ResponseResult;
import com.sj.domain.dto.CategoryDto;
import com.sj.domain.entity.Article;
import com.sj.domain.entity.Category;
import com.sj.domain.vo.AdminCategoryVo;
import com.sj.domain.vo.CategoryVo;
import com.sj.domain.vo.DownloadDataVo;
import com.sj.enums.AppHttpCodeEnum;
import com.sj.mapper.CategoryMapper;
import com.sj.service.ArticleService;
import com.sj.service.CategoryService;
import com.sj.utils.BeanCopyUtils;
import com.sj.utils.DownloadExcelUtils;
import com.sj.utils.WebUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sj.constants.CommonConstants.ARTICLE_STATUS_PUBLISH;
import static com.sj.constants.CommonConstants.CATEGORY_STATUS_NORMAL;
import static com.sj.enums.AppHttpCodeEnum.*;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Resource
    private ArticleService articleService;

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public ResponseResult getCategoryList() {
        // Search for published article in table
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_PUBLISH);
        List<Article> articles = articleService.list(queryWrapper);

        // Get category id and reduce redundant
        Set<Long> categoryIds = articles.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());

        // Search in the category table
        List<Category> categories = listByIds(categoryIds);

        categories = categories.stream()
                .filter(category -> CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        // Encapsulation in VO
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult getArticleCategoryList(Integer pageNum, Integer pageSize, CategoryDto categoryDto) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(categoryDto.getStatus()), Category::getStatus, categoryDto.getStatus());
        queryWrapper.like(StringUtils.hasText(categoryDto.getName()), Category::getName, categoryDto.getName());

        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<Category> categories = page.getRecords();
        List<CategoryTwoVo> categoryTwoVos =
                BeanCopyPropertiesUtils.copyBeanList(categories, CategoryTwoVo.class);

        AdminCategoryVo adminCategoryVo = new AdminCategoryVo(categoryTwoVos, page.getTotal());
        return ResponseResult.okResult(adminCategoryVo);
    }


    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Category::getName, categoryDto.getName());
        Category category = getOne(queryWrapper);
        if (!Objects.isNull(category)) {
            return ResponseResult.errorResult(CATEGORY_IS_EXIST);
        }

        Category addCategory = BeanCopyPropertiesUtils.copyBean(categoryDto, Category.class);
        save(addCategory);
        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult getCategoryOneById(Long id) {
        Category category = getById(id);
        CategoryTwoVo categoryVo = BeanCopyPropertiesUtils.copyBean(category, CategoryTwoVo.class);
        return ResponseResult.okResult(categoryVo);
    }


    @Override
    public ResponseResult updateCategory(CategoryDto categoryDto) {
        if (!StringUtils.hasText(categoryDto.getName()) &&
                !StringUtils.hasText(categoryDto.getDescription()) &&
                !StringUtils.hasText(String.valueOf(categoryDto.getStatus()))) {
            return ResponseResult.errorResult(CONTENT_IS_BLANK);
        }

        Category category = BeanCopyPropertiesUtils.copyBean(categoryDto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult deleteCategory(Long id) {
        boolean result = removeById(id);
        if (result == false) {
            return ResponseResult.errorResult(DELETE_CATEGORY_FAIL);
        }
        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult getNameArticleCategoryList() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, CATEGORY_STATUS_NORMAL);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        List<CategoryVo> categoryVos = BeanCopyPropertiesUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public void getExcelData(String filename, HttpServletResponse response){
        try {
            DownLoadExcelUtils.setDownLoadHeader(filename, response);
            LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Category::getStatus, CATEGORY_STATUS_NORMAL);
            List<Category> categories = categoryMapper.selectList(queryWrapper);
            List<DownloadDataVo> downloadDataVos =
                    BeanCopyPropertiesUtils.copyBeanList(categories, DownloadDataVo.class);
            EasyExcel.write(response.getOutputStream(), DownloadDataVo.class)
                    .autoCloseStream(Boolean.FALSE).sheet("Category")
                    .doWrite(downloadDataVos);
        } catch (Exception e) {
            ResponseResult result = ResponseResult.errorResult(SYSTEM_ERROR);
            WebUtils.renderString(response,JSON.toJSONString(result));
        }
    }


    @Override
    public String getCategoryName(Long id) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId,id);
        String categoryName = getOne(queryWrapper).getName();
        return categoryName;
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, CATEGORY_STATUS_NORMAL);
        List<Category> list = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return categoryVos;
    }
}