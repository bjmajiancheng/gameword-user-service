package com.coolplay.system.system.api.common;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.security.security.SecurityUser;
import com.coolplay.system.security.utils.SecurityUtil;
import com.coolplay.system.system.model.CategoryModel;
import com.coolplay.system.system.model.LabelModel;
import com.coolplay.system.system.service.ICategoryService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by majiancheng on 2019/10/4.
 */
@Controller
@RequestMapping("/api/common/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @ResponseBody
    @RequestMapping(value = "/options", method = RequestMethod.GET)
    public Result options() {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setIsDel(0);

        List<CategoryModel> categoryModels = categoryService.selectByFilter(categoryModel);

        return ResponseUtil.success(PageConvertUtil.grid(categoryModels));
    }

    @ResponseBody
    @RequestMapping(value="/list", method = RequestMethod.GET)
    public Map list(CategoryModel categoryModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {
        categoryModel.setIsDel(0);
        PageInfo<CategoryModel> pageInfo = categoryService.selectByFilterAndPage(categoryModel, pageNum, pageSize);

        return PageConvertUtil.grid(pageInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/saveCategory", method = RequestMethod.POST)
    public Result saveCategory(CategoryModel categoryModel) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("catName", categoryModel.getCatName());
        param.put("isDel", 0);

        List<CategoryModel> categoryModels = categoryService.find(param);
        if(CollectionUtils.isNotEmpty(categoryModels)) {
            return ResponseUtil.error("分类名称不能重复, 请更换其他分类名称!!");
        }

        try{

            categoryModel.setIsDel(0);

            int saveCnt = categoryService.saveNotNull(categoryModel);
        } catch(DuplicateKeyException e) {
            e.printStackTrace();
            return ResponseUtil.error("分类名称不能重复, 请更换其他分类名称!!");
        } catch(Exception e) {
            return ResponseUtil.error("系统异常, 请稍后重试!!");
        }

        return ResponseUtil.success();
    }

    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Result detail(@RequestParam("id") Integer id) {
        CategoryModel categoryModel = categoryService.findById(id);

        return ResponseUtil.success(categoryModel);
    }

    @ResponseBody
    @RequestMapping(value = "/delCategory", method = RequestMethod.GET)
    public Result delCategory(@RequestParam("id") Integer id) {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(id);
        categoryModel.setIsDel(1);

        int updateCnt = categoryService.updateNotNull(categoryModel);

        return ResponseUtil.success();
    }

    @ResponseBody
    @RequestMapping(value = "/updateCategory", method = RequestMethod.POST)
    public Result updateCategory(CategoryModel categoryModel) {
        if(categoryModel == null) {
            return ResponseUtil.error("系统异常, 请稍后重试!!");
        }

        int updateCnt = categoryService.updateNotNull(categoryModel);

        return ResponseUtil.success();
    }
}
