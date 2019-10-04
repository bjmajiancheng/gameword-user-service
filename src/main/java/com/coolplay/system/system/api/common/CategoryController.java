package com.coolplay.system.system.api.common;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.system.model.CategoryModel;
import com.coolplay.system.system.model.LabelModel;
import com.coolplay.system.system.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
}
