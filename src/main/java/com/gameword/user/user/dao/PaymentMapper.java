/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.gameword.user.user.dao;
import com.gameword.user.user.model.PaymentModel;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.*;
import com.gameword.user.user.dao.*;
import com.gameword.user.user.service.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

public interface PaymentMapper extends Mapper<PaymentModel> {

	public List<PaymentModel> find(Map<String, Object> param);

	public PaymentModel findById(@Param("id")Integer id);

}
