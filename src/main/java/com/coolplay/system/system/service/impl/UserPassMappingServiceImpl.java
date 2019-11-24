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
import com.coolplay.system.system.model.UserPassMappingModel;
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

@Service("userPassMappingService")
public class UserPassMappingServiceImpl extends BaseService<UserPassMappingModel> implements IUserPassMappingService{
	@Autowired
	private UserPassMappingMapper userPassMappingMapper;
	
	@Override
	public UserPassMappingModel findById(Integer id) {
		if(id == null) {
			return null;
		}
		return userPassMappingMapper.findById(id);
	}


	public List<UserPassMappingModel> find(Map<String, Object> param) {
		return userPassMappingMapper.find(param);
	}

	@Override
	public PageInfo<UserPassMappingModel> selectByFilterAndPage(UserPassMappingModel userPassMappingModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<UserPassMappingModel> list = this.selectByFilter(userPassMappingModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<UserPassMappingModel> selectByFilter(UserPassMappingModel userPassMappingModel) {
		Example example = new Example(UserPassMappingModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(StringUtils.isNotEmpty(userPassMappingModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(userPassMappingModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}

	public UserPassMappingModel findByPassword(String password) {
		return userPassMappingMapper.findByPassword(password);
	}

	public UserPassMappingModel findByPasswordEncode(String passwordEncode) {
		return userPassMappingMapper.findByPasswordEncode(passwordEncode);
	}
}
