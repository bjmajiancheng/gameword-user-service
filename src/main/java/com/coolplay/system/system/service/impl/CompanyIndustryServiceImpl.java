/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.coolplay.system.system.service.impl;

import com.coolplay.system.common.baseservice.impl.BaseService;
import com.coolplay.system.system.dao.*;
import com.coolplay.system.system.model.CompanyIndustryModel;
import com.coolplay.system.system.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

@Service("companyIndustryService")
public class CompanyIndustryServiceImpl extends BaseService<CompanyIndustryModel> implements ICompanyIndustryService{
	@Autowired
	private CompanyIndustryMapper companyIndustryMapper;
	
	@Override
	public CompanyIndustryModel findById(Integer id) {
		if(id == null) {
			return null;
		}
		return companyIndustryMapper.findById(id);
	}


	public List<CompanyIndustryModel> find(Map<String, Object> param) {
		return companyIndustryMapper.find(param);
	}

	@Override
	public PageInfo<CompanyIndustryModel> selectByFilterAndPage(CompanyIndustryModel companyIndustryModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<CompanyIndustryModel> list = this.selectByFilter(companyIndustryModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<CompanyIndustryModel> selectByFilter(CompanyIndustryModel companyIndustryModel) {
		Example example = new Example(CompanyIndustryModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(StringUtils.isNotEmpty(companyIndustryModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(companyIndustryModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}

	/**
	 * 获取公司行业集合
	 *
	 * @param companyId
	 * @return
	 */
	public List<CompanyIndustryModel> findByCompanyId(Integer companyId) {
		if(companyId == null || companyId == 0) {
			return Collections.emptyList();
		}

		return this.find(Collections.singletonMap("companyId", companyId));
	}

	/**
	 * 根据企业ID删除企业行业关联信息
	 *
	 * @param companyId
	 * @return
	 */
	public int delByCompanyId(Integer companyId) {
		if(companyId == null || companyId == 0) {
			return 0;
		}

		return companyIndustryMapper.delByCompanyId(companyId);
	}

}
