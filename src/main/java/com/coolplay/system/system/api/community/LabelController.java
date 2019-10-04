package com.coolplay.system.system.api.community;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.core.model.UserModel;
import com.coolplay.system.core.model.UserRoleModel;
import com.coolplay.system.security.security.SecurityUser;
import com.coolplay.system.security.utils.SecurityUtil;
import com.coolplay.system.system.model.CategoryModel;
import com.coolplay.system.system.model.CompanyModel;
import com.coolplay.system.system.model.LabelModel;
import com.coolplay.system.system.service.ICategoryService;
import com.coolplay.system.system.service.ILabelService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by majiancheng on 2019/10/4.
 */
@Controller
@RequestMapping("/api/community/label")
public class LabelController {

    @Autowired
    private ILabelService labelService;

    @Autowired
    private ICategoryService categoryService;

    @ResponseBody
    @RequestMapping(value="/list", method = RequestMethod.GET)
    public Map list(LabelModel labelModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {
        labelModel.setIsDel(0);
        PageInfo<LabelModel> pageInfo = labelService.selectByFilterAndPage(labelModel, pageNum, pageSize);

        if(CollectionUtils.isNotEmpty(pageInfo.getList())) {
            List<Integer> catIds = new ArrayList<Integer>();
            for(LabelModel tmpLabel : pageInfo.getList()) {
                if(!catIds.contains(tmpLabel.getCatId())) {
                    catIds.add(tmpLabel.getCatId());
                }
            }

            Map<Integer, CategoryModel> categoryModelMap = categoryService.findMapByIds(catIds);
            for(LabelModel tmpLabel : pageInfo.getList()) {
                CategoryModel categoryModel = categoryModelMap.get(tmpLabel.getCatId());
                if(categoryModel != null) {
                    tmpLabel.setCatName(categoryModel.getCatName());
                }
            }
        }

        return PageConvertUtil.grid(pageInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/saveLabel", method = RequestMethod.POST)
    public Result saveLabel(LabelModel labelModel) {
        try{

            SecurityUser securityUser = SecurityUtil.getCurrentSecurityUser();
            labelModel.setIsDel(0);
            labelModel.setCtime(new Date());
            labelModel.setCreator(securityUser.getDisplayName());

            labelService.saveNotNull(labelModel);
        } catch(DuplicateKeyException e) {
            e.printStackTrace();
            return ResponseUtil.error("标签名不能重复, 请更换其他标签名!!");
        } catch(Exception e) {
            return ResponseUtil.error("系统异常, 请稍后重试!!");
        }

        return ResponseUtil.success();
    }

    @ResponseBody
    @RequestMapping(value = "/getLabel", method = RequestMethod.GET)
    public Result getLabel(@RequestParam("id") Integer id) {
        LabelModel labelModel = labelService.findById(id);
        if(labelModel != null) {
            CategoryModel categoryModel = categoryService.findById(labelModel.getCatId());
            labelModel.setCatName(categoryModel.getCatName());
        }

        return ResponseUtil.success(labelModel);
    }

    @ResponseBody
    @RequestMapping(value = "/delLabel", method = RequestMethod.GET)
    public Result delLabel(@RequestParam("id") Integer id) {
        LabelModel labelModel = new LabelModel();
        labelModel.setId(id);
        labelModel.setIsDel(1);

        int updateCnt = labelService.updateNotNull(labelModel);

        return ResponseUtil.success();
    }

    @ResponseBody
    @RequestMapping(value = "/options", method = RequestMethod.GET)
    public Result options() {
        LabelModel labelModel = new LabelModel();
        labelModel.setIsDel(0);
        labelModel.setStatus(1);

        List<LabelModel> labelModels = labelService.selectByFilter(labelModel);

        return ResponseUtil.success(PageConvertUtil.grid(labelModels));
    }
}
