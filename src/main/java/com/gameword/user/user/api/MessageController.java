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

import com.alibaba.fastjson.JSON;
import com.gameword.user.common.dto.QueryDto;
import com.gameword.user.common.utils.JPushUtil;
import com.gameword.user.common.utils.PageConvertUtil;
import com.gameword.user.common.utils.ResponseUtil;
import com.gameword.user.common.utils.Result;
import com.gameword.user.core.model.UserModel;
import com.gameword.user.security.service.IUserService;
import com.gameword.user.security.utils.SecurityUtil;
import com.gameword.user.user.model.FriendModel;
import com.gameword.user.user.model.HelpModel;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
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

	@Autowired
	private IFriendService friendService;

	@Autowired
	private IUserService userService;

	@Autowired
	private JPushUtil jPushUtil;

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
			messageModel.setUserId(SecurityUtil.getCurrentUserId());
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
			messageModel.setIsRead(1);

			int updateCnt = messageService.updateNotNull(messageModel);

			MessageModel tmpMessage = messageService.findById(queryDto.getId());

			FriendModel friendModel = JSON.parseObject(tmpMessage.getMessageUrl(), FriendModel.class);
			//1：同意， 2：不同意
			if (queryDto.getIsAgree() == 1) {
				int saveCnt = friendService.saveNotNull(friendModel);
			}

			UserModel userModel = userService.findById(friendModel.getFriendUserId());

			MessageModel message = new MessageModel();
			message.setMessageName("添加好友");
			message.setMessageContent(String.format("%s %s添加您为好友", userModel.getNickName(), (queryDto.getIsAgree() == 1) ? "已同意": "已拒绝"));
			message.setMessageType(2);
			message.setUserId(friendModel.getUserId());
			int saveCnt = messageService.saveNotNull(message);

			jPushUtil.sendMessage(friendModel.getUserId(), message.getMessageName(), message.getMessageContent());

			return ResponseUtil.success(Collections.singletonMap("message", tmpMessage));
		} catch(Exception e) {
			e.printStackTrace();

			return ResponseUtil.error();
		}
	}

	/**
	 * 消息读取
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/readMessage", method = RequestMethod.POST)
	public Result readMessage(@RequestBody QueryDto queryDto) {
		try {
			Integer messageId = queryDto.getId();
			MessageModel messageModel = messageService.findById(messageId);
			if (messageModel == null) {
				return ResponseUtil.error("消息为空，请传递正确的消息ID。");
			}

			MessageModel updateMessage = new MessageModel();
			updateMessage.setId(messageId);
			updateMessage.setIsRead(1);
			messageService.updateNotNull(updateMessage);

			return ResponseUtil.success();
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseUtil.error();
		}
	}

	/**
	 * 消息读取
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/readAll", method = RequestMethod.POST)
	public Result readAll(@RequestBody QueryDto queryDto) {
		try {
			int userId = SecurityUtil.getCurrentUserId();
			MessageModel messageModel = new MessageModel();
			messageModel.setUserId(userId);
			List<MessageModel> messageModels = messageService.selectByFilter(messageModel);
			if (CollectionUtils.isNotEmpty(messageModels)) {
				for (MessageModel tmpMessage : messageModels) {
					tmpMessage.setIsRead(1);

					messageService.updateNotNull(tmpMessage);
				}
			}

			return ResponseUtil.success();
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseUtil.error();
		}
	}
}
