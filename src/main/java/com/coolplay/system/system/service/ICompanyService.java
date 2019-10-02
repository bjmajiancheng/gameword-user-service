package com.coolplay.system.system.service;

import com.coolplay.system.common.baseservice.IBaseService;
import com.coolplay.system.system.model.CompanyModel;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by majiancheng on 2019/9/19.
 */
public interface ICompanyService extends IBaseService<CompanyModel> {

    public CompanyModel findCompanyById(Integer id);

    public PageInfo<CompanyModel> selectByFilterAndPage(CompanyModel companyModel,
            int pageNum, int pageSize);

    public List<CompanyModel> selectByFilter(CompanyModel companyModel);
}
