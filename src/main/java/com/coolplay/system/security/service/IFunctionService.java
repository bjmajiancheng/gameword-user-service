package com.coolplay.system.security.service;

import com.coolplay.system.common.baseservice.IBaseService;
import com.coolplay.system.common.utils.TreeNode;
import com.coolplay.system.core.model.FunctionModel;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by majiancheng on 2019/9/18.
 */
public interface IFunctionService extends IBaseService<FunctionModel> {

    public PageInfo<FunctionModel> selectByFilterAndPage(FunctionModel functionModel, int pageNum, int pageSize);

    public List<FunctionModel> selectByFilter(FunctionModel functionModel);

    public FunctionModel selectById(int id);

    List<TreeNode> getFunctionTreeNodes(FunctionModel functionModel);
}
