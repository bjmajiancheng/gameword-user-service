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
import com.coolplay.system.system.model.CompanyUserModel;
import tk.mybatis.mapper.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import com.coolplay.system.system.dao.*;
import com.coolplay.system.system.service.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

@Service("companyUserService")
public class CompanyUserServiceImpl extends BaseService<CompanyUserModel> implements ICompanyUserService{
	@Autowired
	private CompanyUserMapper companyUserMapper;
	
	@Override
	public CompanyUserModel findById(Integer id) {
		if(id == null) {
			return null;
		}
		return companyUserMapper.findById(id);
	}


	public List<CompanyUserModel> find(Map<String, Object> param) {
		return companyUserMapper.find(param);
	}

	@Override
	public PageInfo<CompanyUserModel> selectByFilterAndPage(CompanyUserModel companyUserModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<CompanyUserModel> list = this.selectByFilter(companyUserModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<CompanyUserModel> selectByFilter(CompanyUserModel companyUserModel) {
		Example example = new Example(CompanyUserModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(companyUserModel.getCompanyId() != null) {
			criteria.andEqualTo("companyId", companyUserModel.getCompanyId());
		}

		if(StringUtils.isNotEmpty(companyUserModel.getUserName())) {
			criteria.andLike("userName", "%" + companyUserModel.getUserName() + "%");
		}

		if(StringUtils.isNotEmpty(companyUserModel.getContactPhone())) {
			criteria.andEqualTo("contactPhone", companyUserModel.getContactPhone());
		}

		if(StringUtils.isNotEmpty(companyUserModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(companyUserModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}


	@Override
	public int delById(Integer userId) {
		return companyUserMapper.delById(userId);
	}

	@Override
	public CompanyUserModel findByUserName(String userName) {
		return companyUserMapper.findByUserName(userName);
	}
}
