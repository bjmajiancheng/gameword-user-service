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
import com.coolplay.system.system.model.IndustryModel;
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

@Service("industryService")
public class IndustryServiceImpl extends BaseService<IndustryModel> implements IIndustryService{
	@Autowired
	private IndustryMapper industryMapper;
	
	@Override
	public IndustryModel findById(Integer id) {
		if(id == null) {
			return null;
		}
		return industryMapper.findById(id);
	}


	public List<IndustryModel> find(Map<String, Object> param) {
		return industryMapper.find(param);
	}

	@Override
	public PageInfo<IndustryModel> selectByFilterAndPage(IndustryModel industryModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<IndustryModel> list = this.selectByFilter(industryModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<IndustryModel> selectByFilter(IndustryModel industryModel) {
		Example example = new Example(IndustryModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(StringUtils.isNotEmpty(industryModel.getIndustryName())) {
			criteria.andLike("industryName", "%" + industryModel.getIndustryName().trim() + "%");
		}

		if(industryModel.getIsDel() != null) {
			criteria.andEqualTo("isDel", industryModel.getIsDel());
		}

		if(StringUtils.isNotEmpty(industryModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(industryModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}

	/**
	 * 根据行业名称和id查询总数
	 *
	 * @param industryName
	 * @param id
	 * @return
	 */
	public int findCntByIndustryNameAndId(String industryName, Integer id) {
		if(StringUtils.isEmpty(industryName)) {
			return 0;
		}

		return industryMapper.findCntByIndustryNameAndId(industryName, id);
	}
}
