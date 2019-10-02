package com.coolplay.system.system.service;

import com.coolplay.system.common.baseservice.IBaseService;
import com.coolplay.system.system.model.CompanyDeptModel;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by majiancheng on 2019/9/19.
 */
public interface ICompanyDeptService extends IBaseService<CompanyDeptModel> {

    public PageInfo<CompanyDeptModel> selectByFilterAndPage(CompanyDeptModel companyDeptModel, int pageNum,
            int pageSize);

    public List<CompanyDeptModel> selectByFilter(CompanyDeptModel companyDeptModel);

    public CompanyDeptModel selectById(int id);
}
