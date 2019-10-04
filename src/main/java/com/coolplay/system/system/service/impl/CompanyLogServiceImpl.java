/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.coolplay.system.system.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coolplay.system.common.baseservice.impl.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.coolplay.system.system.model.CompanyLogModel;
import tk.mybatis.mapper.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;

import java.util.*;

import com.coolplay.system.system.dao.*;
import com.coolplay.system.system.service.*;

/**
 * @author davdian
 * @version 1.0
 * @since 1.0
 */

@Service("companyLogService")
public class CompanyLogServiceImpl extends BaseService<CompanyLogModel> implements ICompanyLogService {

    @Autowired
    private CompanyLogMapper companyLogMapper;

    @Override
    public CompanyLogModel findById(Integer id) {
        if (id == null) {
            return null;
        }
        return companyLogMapper.findById(id);
    }

    public List<CompanyLogModel> find(Map<String, Object> param) {
        return companyLogMapper.find(param);
    }

    @Override
    public PageInfo<CompanyLogModel> selectByFilterAndPage(CompanyLogModel companyLogModel, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CompanyLogModel> list = this.selectByFilter(companyLogModel);
        return new PageInfo<>(list);
    }

    @Override
    public List<CompanyLogModel> selectByFilter(CompanyLogModel companyLogModel) {
        Example example = new Example(CompanyLogModel.class);
        Example.Criteria criteria = example.createCriteria();

        if (companyLogModel.getUserId() != null) {
            criteria.andEqualTo("userId", companyLogModel.getUserId());
        }

        if (StringUtils.isNotEmpty(companyLogModel.getSortWithOutOrderBy())) {
            example.setOrderByClause(companyLogModel.getSortWithOutOrderBy());
        }
        return getMapper().selectByExample(example);
    }
}
