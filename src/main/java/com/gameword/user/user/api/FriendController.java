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
import com.gameword.user.common.utils.Pinyin4jUtil;
import com.gameword.user.common.utils.ResponseUtil;
import com.gameword.user.common.utils.Result;
import com.gameword.user.core.model.UserModel;
import com.gameword.user.security.service.IUserService;
import com.gameword.user.security.utils.SecurityUtil;
import com.gameword.user.user.model.CityModel;
import com.gameword.user.user.model.CountryModel;
import com.gameword.user.user.model.MessageModel;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.gameword.user.user.model.FriendModel;

import java.util.*;
import com.gameword.user.user.dao.*;
import com.gameword.user.user.service.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "/api/friend")
public class FriendController {

	@Autowired
	private IFriendService friendService;

	@Autowired
	private IUserService userService;

	@Autowired
	private ICountryService countryService;

	@Autowired
	private ICityService cityService;

	@Autowired
	private IMessageService messageService;

	@Autowired
	private JPushUtil jPushUtil;

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Result list(@RequestBody QueryDto queryDto) {

		Integer currUserId = SecurityUtil.getCurrentUserId();

		if(currUserId == 0) {
			return ResponseUtil.error("当前用户未登录, 请先登录账号");
		}

		try {
			FriendModel friendModel = new FriendModel();
			friendModel.setUserId(currUserId);
			List<FriendModel> friends = friendService.selectByFilter(friendModel);
			if(CollectionUtils.isNotEmpty(friends)) {
				List<Integer> friendUserIds = new ArrayList<Integer>();
				for(FriendModel tmpFriend : friends) {
					friendUserIds.add(tmpFriend.getFriendUserId());
				}

				Map<Integer, UserModel> userMap = userService.findUserMapByUserIds(friendUserIds);
				Set<Integer> countryIds = new HashSet<>();
				Set<Integer> cityIds = new HashSet<>();

				if(MapUtils.isNotEmpty(userMap)) {
					for(UserModel userModel : userMap.values()) {
						countryIds.add(userModel.getCountryId());
						cityIds.add(userModel.getCityId());
					}
				}

				Map<Integer, CountryModel> countryMap = countryService.findMapByCountryIds(new ArrayList<Integer>(countryIds));
				Map<Integer, CityModel> cityMap = cityService.findMapByCityIds(new ArrayList<Integer>(cityIds));

				for(FriendModel tmpFriend : friends) {
					UserModel tmpUser = userMap.get(tmpFriend.getFriendUserId());
					if(tmpUser != null) {
						tmpFriend.setFriendHeadImage(tmpUser.getHeadImage());
						tmpFriend.setFriendNickName(tmpUser.getNickName());

						CountryModel tmpCountry = countryMap.get(tmpUser.getCountryId());
						CityModel tmpCity = cityMap.get(tmpUser.getCityId());

						if(tmpCountry != null) {
							tmpFriend.setFriendCountryCnName(tmpCountry.getCountryCnName());
							tmpFriend.setFriendCountryEnName(tmpCountry.getCountryEnName());
						}

						if(tmpCity != null) {
							tmpFriend.setFriendCityCnName(tmpCity.getCityCn());
							tmpFriend.setFriendCityEnName(tmpCity.getCityEn());
						}
					}
				}

			}

			return ResponseUtil.success(Collections.singletonMap("friends", friendService.generKeyWordFriendMap(friends)));
		} catch(Exception e) {
			e.printStackTrace();

			return ResponseUtil.error();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/friendList", method = RequestMethod.POST)
	public Result friendList(@RequestBody QueryDto queryDto) {

		Integer currUserId = SecurityUtil.getCurrentUserId();

		if(currUserId == null || currUserId == 0) {
			return ResponseUtil.error("当前用户未登录, 请先登录账号");
		}

		try {
			FriendModel friendModel = new FriendModel();
			friendModel.setUserId(currUserId);
			List<FriendModel> friends = friendService.selectByFilter(friendModel);
			if(CollectionUtils.isNotEmpty(friends)) {
				List<Integer> friendUserIds = new ArrayList<>();
				for(FriendModel tmpFriend : friends) {
					friendUserIds.add(tmpFriend.getFriendUserId());
				}

				Map<Integer, UserModel> userMap = userService.findUserMapByUserIds(friendUserIds);
				Set<Integer> countryIds = new HashSet<>();
				Set<Integer> cityIds = new HashSet<>();

				if(MapUtils.isNotEmpty(userMap)) {
					for(UserModel userModel : userMap.values()) {
						countryIds.add(userModel.getCountryId());
						cityIds.add(userModel.getCityId());
					}
				}

				Map<Integer, CountryModel> countryMap = countryService.findMapByCountryIds(new ArrayList<>(countryIds));
				Map<Integer, CityModel> cityMap = cityService.findMapByCityIds(new ArrayList<>(cityIds));

				for(FriendModel tmpFriend : friends) {
					UserModel tmpUser = userMap.get(tmpFriend.getFriendUserId().intValue());
					if(tmpUser != null) {
						tmpFriend.setFriendHeadImage(tmpUser.getHeadImage());
						tmpFriend.setFriendNickName(tmpUser.getNickName());
						tmpFriend.setFriendSex(tmpUser.getSex());
						tmpFriend.setFriendAgencyName(tmpUser.getAgencyName());
						tmpFriend.setFriendUserDesc(tmpUser.getUserDesc());

						CountryModel tmpCountry = countryMap.get(tmpUser.getCountryId().intValue());
						CityModel tmpCity = cityMap.get(tmpUser.getCityId().intValue());

						if(tmpCountry != null) {
							tmpFriend.setFriendCountryCnName(tmpCountry.getCountryCnName());
							tmpFriend.setFriendCountryEnName(tmpCountry.getCountryEnName());
							tmpFriend.setFriendCountryFlag(tmpCountry.getCountryFlag());
						}

						if(tmpCity != null) {
							tmpFriend.setFriendCityCnName(tmpCity.getCityCn());
							tmpFriend.setFriendCityEnName(tmpCity.getCityEn());
						}
					}

					String keyword = Pinyin4jUtil.getPinYinFirstChar(tmpFriend.getFriendNickName());
					tmpFriend.setFirstCharPinyin(keyword);
				}

			}

			return ResponseUtil.success(Collections.singletonMap("friends", friends));
		} catch(Exception e) {
			e.printStackTrace();

			return ResponseUtil.error();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/addFriend", method = RequestMethod.POST)
	public Result addFriend(@RequestBody QueryDto queryDto) {

		Integer currUserId = SecurityUtil.getCurrentUserId();

		try {
			FriendModel friendModel = new FriendModel();
			friendModel.setUserId(currUserId);
			friendModel.setFriendUserId(queryDto.getFriendUserId());

			if(CollectionUtils.isNotEmpty(friendService.selectByFilter(friendModel))) {
				return ResponseUtil.error("已添加该好友, 禁止重复添加好友");
			}

			MessageModel messageModel = new MessageModel();
			messageModel.setUserId(queryDto.getFriendUserId());
			messageModel.setMessageName("添加好友");
			messageModel.setMessageContent("添加您为好友，是否同意？");
			messageModel.setMessageType(1);
			messageModel.setMessageUrl(JSON.toJSONString(friendModel));

			int saveCnt = messageService.saveNotNull(messageModel);

			//极光推送消息
			jPushUtil.sendMessage(queryDto.getFriendUserId(), messageModel.getMessageName(), messageModel.getMessageContent());

			/*int saveCnt = friendService.saveNotNull(friendModel);*/

			return ResponseUtil.success();
		} catch(Exception e) {
			e.printStackTrace();

			return ResponseUtil.error();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delFriend", method = RequestMethod.POST)
	public Result delFriend(@RequestBody QueryDto queryDto) {

		Integer currUserId = SecurityUtil.getCurrentUserId();

		try {
			FriendModel friendModel = new FriendModel();
			friendModel.setUserId(currUserId);
			friendModel.setFriendUserId(queryDto.getFriendUserId());
			List<FriendModel> friendModels = friendService.selectByFilter(friendModel);
			if(CollectionUtils.isEmpty(friendModels)) {
				return ResponseUtil.error("未添加该好友, 无法删除");
			}

			friendModel = friendModels.get(0);
			friendService.delete(friendModel);

			return ResponseUtil.success();
		} catch(Exception e) {
			e.printStackTrace();

			return ResponseUtil.error();
		}
	}
	@ResponseBody
	@RequestMapping(value = "/noteUser", method = RequestMethod.POST)
	public Result noteUser(@RequestBody QueryDto queryDto) {
		Integer friendUserId = queryDto.getUserId();
		String noteName = queryDto.getNoteName();
		if(friendUserId == null || StringUtils.isEmpty(noteName)) {
			return ResponseUtil.error("请填写好友备注信息");
		}

		try {
			Integer userId = SecurityUtil.getCurrentUserId();

			int updateCnt = friendService.updateNoteName(userId, friendUserId, noteName);

			return ResponseUtil.success();
		} catch(Exception e) {
			e.printStackTrace();

			return ResponseUtil.error();
		}
	}
}
