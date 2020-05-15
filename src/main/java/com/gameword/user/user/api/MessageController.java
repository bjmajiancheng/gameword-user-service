/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.gameword.user.user.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gameword.user.common.dto.QueryDto;
import com.gameword.user.common.utils.PageConvertUtil;
import com.gameword.user.common.utils.ResponseUtil;
import com.gameword.user.common.utils.Result;
import com.gameword.user.user.model.HelpModel;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gameword.user.user.model.MessageModel;

import java.util.*;
import com.gameword.user.user.dao.*;
import com.gameword.user.user.service.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "/api/message")
public class MessageController {

	@Autowired
	private IMessageService messageService;

	/**
	 * 消息列表
	 *
	 * @param queryDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Result list(@RequestBody QueryDto queryDto) {
		try {
			MessageModel messageModel = new MessageModel();
			messageModel.setSort_("c_time_asc");

			PageInfo<MessageModel> pageInfo = messageService.selectByFilterAndPage(messageModel, queryDto.getPageNum(), queryDto.getPageSize());

			return ResponseUtil.success(PageConvertUtil.grid(pageInfo));
		} catch(Exception e) {
			e.printStackTrace();

			return ResponseUtil.error();
		}
	}

	/**
	 * 消息处理
	 *
	 * @param queryDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/dealMessage", method = RequestMethod.POST)
	public Result dealMessage(@RequestBody QueryDto queryDto) {
		try {
			MessageModel messageModel = new MessageModel();
			messageModel.setId(queryDto.getId());
			messageModel.setIsAgree(queryDto.getIsAgree());

			int updateCnt = messageService.updateNotNull(messageModel);

			MessageModel tmpMessage = messageService.findById(queryDto.getId());

			return ResponseUtil.success(Collections.singletonMap("message", tmpMessage));
		} catch(Exception e) {
			e.printStackTrace();

			return ResponseUtil.error();
		}
	}
}
