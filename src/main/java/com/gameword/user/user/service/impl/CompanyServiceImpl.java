/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.gameword.user.user.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gameword.user.common.baseservice.impl.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gameword.user.user.model.CompanyModel;
import tk.mybatis.mapper.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import com.gameword.user.user.dao.*;
import com.gameword.user.user.service.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

@Service("companyService")
public class CompanyServiceImpl extends BaseService<CompanyModel> implements ICompanyService{
	@Autowired
	private CompanyMapper companyMapper;
	
	@Override
	public CompanyModel findById(Integer id) {
		if(id == null) {
			return null;
		}
		return companyMapper.findById(id);
	}


	public List<CompanyModel> find(Map<String, Object> param) {
		return companyMapper.find(param);
	}

	@Override
	public PageInfo<CompanyModel> selectByFilterAndPage(CompanyModel companyModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<CompanyModel> list = this.selectByFilter(companyModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<CompanyModel> selectByFilter(CompanyModel companyModel) {
		Example example = new Example(CompanyModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(companyModel.getCityId() != null) {
			criteria.andEqualTo("cityId", companyModel.getCityId());
		}

		if(StringUtils.isNotEmpty(companyModel.getCnName())) {
			criteria.andLike("cnName", "%" + companyModel.getCnName() + "%");
		}

		if(StringUtils.isNotEmpty(companyModel.getEnName())) {
			criteria.andLike("enName", "%" + companyModel.getEnName() + "%");
		}

		if(StringUtils.isNotEmpty(companyModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(companyModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}
}
