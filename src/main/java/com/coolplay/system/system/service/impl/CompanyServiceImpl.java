package com.coolplay.system.system.service.impl;

import com.coolplay.system.common.baseservice.impl.BaseService;
import com.coolplay.system.system.dao.CompanyMapper;
import com.coolplay.system.system.model.CompanyModel;
import com.coolplay.system.system.service.ICompanyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by majiancheng on 2019/9/19.
 */
@Service("companyService")
public class CompanyServiceImpl extends BaseService<CompanyModel> implements ICompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    public CompanyModel findCompanyById(Integer id) {
        return companyMapper.findCompanyById(id);
    }

    @Override
    public PageInfo<CompanyModel> selectByFilterAndPage(CompanyModel companyModel, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CompanyModel> list = this.selectByFilter(companyModel);
        return new PageInfo<>(list);
    }

    @Override
    public List<CompanyModel> selectByFilter(CompanyModel companyModel) {
        Example example = new Example(CompanyModel.class);
        Example.Criteria criteria = example.createCriteria();

        if(companyModel.getIsDel() != null) {
            criteria.andEqualTo("isDel", companyModel.getIsDel());
        }

        if(StringUtils.isNotEmpty(companyModel.getContactMobile())) {
            criteria.andLike("contactMobile", "%" + companyModel.getContactMobile() + "%");
        }

        if(StringUtils.isNotEmpty(companyModel.getSortWithOutOrderBy())) {
            example.setOrderByClause(companyModel.getSortWithOutOrderBy());
        }
        return getMapper().selectByExample(example);
    }
}
