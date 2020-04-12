/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.gameword.user.user.service;

import com.gameword.user.common.baseservice.IBaseService;
import com.gameword.user.user.model.FeedbackModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.*;
import com.gameword.user.user.dao.*;
import com.gameword.user.user.service.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

public interface IFeedbackService extends IBaseService<FeedbackModel> {

	public FeedbackModel findById(Integer id);

	public List<FeedbackModel> find(Map<String, Object> param);

	public PageInfo<FeedbackModel> selectByFilterAndPage(FeedbackModel feedbackModel, int pageNum, int pageSize);

	public List<FeedbackModel> selectByFilter(FeedbackModel feedbackModel);

}
