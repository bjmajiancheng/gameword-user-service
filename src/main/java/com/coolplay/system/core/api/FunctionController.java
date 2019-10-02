package com.coolplay.system.core.api;

import com.alibaba.druid.util.StringUtils;
import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.core.model.FunctionModel;
import com.coolplay.system.security.service.IFunctionService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by majiancheng on 2019/9/30.
 */
@Controller
@RequestMapping("/api/core/function")
public class FunctionController {

    @Autowired
    private IFunctionService functionService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map list(FunctionModel functionModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {
        PageInfo<FunctionModel> pageInfo = functionService.selectByFilterAndPage(functionModel, pageNum, pageSize);

        return PageConvertUtil.grid(pageInfo);
    }

    /**
     * 获取公司部门信息
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getFunction", method = RequestMethod.GET)
    public Result getFunction(@RequestParam("id") int id) {
        FunctionModel functionModel = functionService.selectById(id);
        functionModel.setParentFunctionName("");

        if (functionModel != null && functionModel.getParentId() != null) {
            FunctionModel parentFunctionModel = functionService.selectById(functionModel.getParentId());
            if (parentFunctionModel != null) {
                functionModel.setParentFunctionName(parentFunctionModel.getFunctionName());
            }
        }

        return ResponseUtil.success(functionModel);
    }

    /**
     * 添加公司部门信息
     *
     * @param functionModel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveFunction", method = RequestMethod.POST)
    public Result saveFunction(FunctionModel functionModel) {
        functionModel.setStatus(1);
        functionModel.setDisplay(1);
        if (StringUtils.isEmpty(functionModel.getIcon())) {
            functionModel.setIcon("");
        }
        if(functionModel.getParentId() == null) {
            functionModel.setParentId(0);
        }
        int addCnt = functionService.save(functionModel);

        return ResponseUtil.success();
    }

    /**
     * 添加公司部门信息
     *
     * @param functionModel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateFunction", method = RequestMethod.POST)
    public Result updateFunction(FunctionModel functionModel) {
        int updateCnt = functionService.updateNotNull(functionModel);

        return ResponseUtil.success();
    }

    /**
     * 禁用或启用公司部门
     *
     * @param id
     * @param status
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/disableFunction", method = RequestMethod.GET)
    public Result disableFunction(@RequestParam("id") int id, @RequestParam("status") int status) {
        FunctionModel functionModel = new FunctionModel();
        functionModel.setId(id);
        functionModel.setStatus(status);
        int updateCnt = functionService.updateNotNull(functionModel);

        return ResponseUtil.success();
    }

    @ResponseBody
    @RequestMapping(value = "/treeNodes", method = RequestMethod.POST)
    public Object treeNodes(FunctionModel functionModel) {
        return functionService.getFunctionTreeNodes(functionModel);
    }
}

