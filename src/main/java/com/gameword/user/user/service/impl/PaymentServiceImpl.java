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
import com.gameword.user.user.model.PaymentModel;
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

@Service("paymentService")
public class PaymentServiceImpl extends BaseService<PaymentModel> implements IPaymentService{
	@Autowired
	private PaymentMapper paymentMapper;
	
	@Override
	public PaymentModel findById(Integer id) {
		if(id == null) {
			return null;
		}
		return paymentMapper.findById(id);
	}


	public List<PaymentModel> find(Map<String, Object> param) {
		return paymentMapper.find(param);
	}

	@Override
	public PageInfo<PaymentModel> selectByFilterAndPage(PaymentModel paymentModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<PaymentModel> list = this.selectByFilter(paymentModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<PaymentModel> selectByFilter(PaymentModel paymentModel) {
		Example example = new Example(PaymentModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(StringUtils.isNotEmpty(paymentModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(paymentModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}
}
