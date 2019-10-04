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
import com.coolplay.system.system.model.PostCommentModel;
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

@Service("postCommentService")
public class PostCommentServiceImpl extends BaseService<PostCommentModel> implements IPostCommentService{
	@Autowired
	private PostCommentMapper postCommentMapper;
	
	@Override
	public PostCommentModel findById(Integer id) {
		if(id == null) {
			return null;
		}
		return postCommentMapper.findById(id);
	}


	public List<PostCommentModel> find(Map<String, Object> param) {
		return postCommentMapper.find(param);
	}

	@Override
	public PageInfo<PostCommentModel> selectByFilterAndPage(PostCommentModel postCommentModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<PostCommentModel> list = this.selectByFilter(postCommentModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<PostCommentModel> selectByFilter(PostCommentModel postCommentModel) {
		Example example = new Example(PostCommentModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(postCommentModel.getPostId() != null) {
			criteria.andEqualTo("postId", postCommentModel.getPostId());
		}

		if(postCommentModel.getIsDel() != null) {
			criteria.andEqualTo("isDel", postCommentModel.getIsDel());
		}

		if(StringUtils.isNotEmpty(postCommentModel.getCommentContent())) {
			criteria.andLike("commentContent", "%" + postCommentModel.getCommentContent() + "%");
		}

		if(StringUtils.isNotEmpty(postCommentModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(postCommentModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}
}
