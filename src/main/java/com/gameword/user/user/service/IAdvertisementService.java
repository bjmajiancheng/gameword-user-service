/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.gameword.user.user.service;

import com.gameword.user.common.baseservice.IBaseService;
import com.gameword.user.user.model.AdvertisementModel;
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

public interface IAdvertisementService extends IBaseService<AdvertisementModel> {

	public AdvertisementModel findById(Integer id);

	public List<AdvertisementModel> find(Map<String, Object> param);

	public PageInfo<AdvertisementModel> selectByFilterAndPage(AdvertisementModel advertisementModel, int pageNum,
		int pageSize);

	public List<AdvertisementModel> selectByFilter(AdvertisementModel advertisementModel);

}
