/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.coolplay.system.system.dao;
import com.coolplay.system.system.model.IndustryModel;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.*;
import com.coolplay.system.system.dao.*;
import com.coolplay.system.system.service.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

public interface IndustryMapper extends Mapper<IndustryModel> {

	public List<IndustryModel> find(Map<String, Object> param);

	public IndustryModel findById(@Param("id") Integer id);

	/**
	 * 根据行业名称和id查询总数
	 *
	 * @param industryName
	 * @param id
	 * @return
	 */
	public int findCntByIndustryNameAndId(@Param("industryName")String industryName, @Param("id")Integer id);
}
