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
import com.coolplay.system.system.model.CategoryModel;
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

@Service("categoryService")
public class CategoryServiceImpl extends BaseService<CategoryModel> implements ICategoryService{
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
	public CategoryModel findById(Integer id) {
		if(id == null) {
			return null;
		}
		return categoryMapper.findById(id);
	}


	public List<CategoryModel> find(Map<String, Object> param) {
		return categoryMapper.find(param);
	}

	@Override
	public PageInfo<CategoryModel> selectByFilterAndPage(CategoryModel categoryModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<CategoryModel> list = this.selectByFilter(categoryModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<CategoryModel> selectByFilter(CategoryModel categoryModel) {
		Example example = new Example(CategoryModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(StringUtils.isNotEmpty(categoryModel.getCatName())) {
			criteria.andLike("catName", categoryModel.getCatName());
		}

		if(categoryModel.getIsDel() != null) {
			criteria.andEqualTo("isDel", categoryModel.getIsDel());
		}

		if(StringUtils.isNotEmpty(categoryModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(categoryModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}


	public Map<Integer, CategoryModel> findMapByIds(List<Integer> ids) {
		if(CollectionUtils.isEmpty(ids)) {
			return Collections.emptyMap();
		}

		List<CategoryModel> categorys = this.find(Collections.singletonMap("ids", ids));
		if(CollectionUtils.isEmpty(categorys)) {
			return Collections.emptyMap();
		}
		Map<Integer, CategoryModel> categoryModelMap = new HashMap<Integer, CategoryModel>();
		for(CategoryModel categoryModel : categorys) {
			categoryModelMap.put(categoryModel.getId(), categoryModel);
		}

		return categoryModelMap;
	}
}
