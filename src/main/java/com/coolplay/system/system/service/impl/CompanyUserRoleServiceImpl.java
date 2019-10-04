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
import com.coolplay.system.system.model.CompanyUserRoleModel;
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

@Service("companyUserRoleService")
public class CompanyUserRoleServiceImpl extends BaseService<CompanyUserRoleModel> implements ICompanyUserRoleService{
	@Autowired
	private CompanyUserRoleMapper companyUserRoleMapper;
	

	public List<CompanyUserRoleModel> find(Map<String, Object> param) {
		return companyUserRoleMapper.find(param);
	}

	@Override
	public PageInfo<CompanyUserRoleModel> selectByFilterAndPage(CompanyUserRoleModel companyUserRoleModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<CompanyUserRoleModel> list = this.selectByFilter(companyUserRoleModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<CompanyUserRoleModel> selectByFilter(CompanyUserRoleModel companyUserRoleModel) {
		Example example = new Example(CompanyUserRoleModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(StringUtils.isNotEmpty(companyUserRoleModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(companyUserRoleModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}


	public List<CompanyUserRoleModel> findByUserId(Integer userId) {
		if(userId == null) {
			return Collections.emptyList();
		}

		return companyUserRoleMapper.findByUserId(userId);
	}
}
