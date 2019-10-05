/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.coolplay.system.system.service;

import com.coolplay.system.common.baseservice.IBaseService;
import com.coolplay.system.common.utils.TreeNode;
import com.coolplay.system.system.model.HelpModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.*;
import com.coolplay.system.system.dao.*;
import com.coolplay.system.system.service.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

public interface IHelpService extends IBaseService<HelpModel> {

	public HelpModel findById(Integer id);

	public List<HelpModel> find(Map<String, Object> param);

	public PageInfo<HelpModel> selectByFilterAndPage(HelpModel helpModel, int pageNum, int pageSize);

	public List<HelpModel> selectByFilter(HelpModel helpModel);

	public List<TreeNode> getHelpTreeNodes(HelpModel helpModel);

}
