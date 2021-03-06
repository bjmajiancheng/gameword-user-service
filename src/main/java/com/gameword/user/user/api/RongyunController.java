package com.gameword.user.user.api;

import com.gameword.user.common.dto.QueryDto;
import com.gameword.user.common.utils.*;
import com.gameword.user.core.model.UserModel;
import com.gameword.user.security.service.IUserService;
import com.gameword.user.security.utils.SecurityUtil;
import com.gameword.user.user.dto.ChatroomUserDto;
import com.gameword.user.user.model.CityModel;
import com.gameword.user.user.model.CountryModel;
import com.gameword.user.user.model.FriendModel;
import com.gameword.user.user.model.RegionModel;
import com.gameword.user.user.service.ICityService;
import com.gameword.user.user.service.ICountryService;
import com.gameword.user.user.service.IFriendService;
import com.gameword.user.user.service.IRegionService;
import io.rong.methods.chatroom.Chatroom;
import io.rong.models.chatroom.ChatroomMember;
import io.rong.models.response.ChatroomUserQueryResult;
import io.rong.models.response.ListBlockChatroomUserResult;
import io.rong.models.response.ResponseResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by majiancheng on 2020/5/9.
 */
@Controller
@RequestMapping(value = "/api/rongyun")
public class RongyunController {


    @Autowired
    private RongyunUtil rongyunUtil;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICountryService countryService;

    @Autowired
    private ICityService cityService;

    @Autowired
    private IFriendService friendService;

    @Autowired
    private IRegionService regionService;
    /**
     * 创建聊天室
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createChatroom", method = RequestMethod.POST)
    public Result createChatroom(@RequestBody QueryDto queryDto) {
        if(StringUtils.isEmpty(queryDto.getChatroomId()) || StringUtils.isEmpty(queryDto.getChatroomName())) {
            return ResponseUtil.error("房间ID或者房间名称不能为空");
        }

        try {
            ResponseResult result = rongyunUtil.createChatroom(queryDto.getChatroomId(), queryDto.getChatroomName());

            return ResponseUtil.success(result.getErrorMessage());
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error();
        }
    }

    /**
     * 注销聊天室
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/destoryChatroom", method = RequestMethod.POST)
    public Result destoryChatroom(@RequestBody QueryDto queryDto) {
        if(StringUtils.isEmpty(queryDto.getChatroomId())) {
            return ResponseUtil.error("房间ID不能为空");
        }

        try {
            ResponseResult result = rongyunUtil.destoryChatroom(queryDto.getChatroomId());

            return ResponseUtil.success(result.getErrorMessage());
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error();
        }
    }

    /**
     * 聊天室成员
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/chatroomUsers", method = RequestMethod.POST)
    public Result chatroomUsers(@RequestBody QueryDto queryDto) {
        if(StringUtils.isEmpty(queryDto.getChatroomId())) {
            return ResponseUtil.error("房间ID不能为空");
        }

        try {
            ChatroomUserQueryResult result = rongyunUtil.chatroomUsers(queryDto.getChatroomId());
            List<ChatroomMember> chatroomMembers = result.getMembers();

            ListBlockChatroomUserResult blockUserRes = rongyunUtil.blockUsers(queryDto.getChatroomId());
            List<ChatroomMember> blockMembers = blockUserRes.getMembers();
            Map<String, ChatroomMember> blockMemberMap = new HashMap<>();
            blockMembers.forEach(e -> blockMemberMap.put(e.getId(), e));

            List<UserModel> userModels = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(chatroomMembers)) {
                List<Integer> userIds = new ArrayList<>();
                for(ChatroomMember chatroomMember : chatroomMembers) {
                    userIds.add(Integer.valueOf(chatroomMember.getId()));
                }

                Map<String, Object> param = new HashMap<>();
                if (StringUtils.isNotBlank(queryDto.getNickName())) {
                    param.put("nickName", "%" + queryDto.getNickName() + "%");
                }
                param.put("userIds", userIds);
                if (CollectionUtils.isNotEmpty(userIds)) {
                    userModels = userService.find(param);
                } else {
                    userModels = Collections.emptyList();
                }
            }

            List<ChatroomUserDto> roomUsers = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(userModels)) {
                List<Integer> userIds = new ArrayList<>();

                Set<Integer> countryIds = new HashSet<>();
                Set<Integer> cityIds = new HashSet<>();
                Set<Integer> regionIds = new HashSet<>();

                for(UserModel tmpUser : userModels) {
                    userIds.add(tmpUser.getId());

                    countryIds.add(tmpUser.getCountryId());
                    cityIds.add(tmpUser.getCityId());
                }

                regionIds.addAll(countryIds);
                regionIds.addAll(cityIds);
                Map<Integer, CountryModel> countryMap = countryService.findMapByCountryIds(new ArrayList<>(countryIds));
                /*Map<Integer, CityModel> cityMap = cityService.findMapByCityIds(new ArrayList<>(cityIds));*/

                Map<Integer, RegionModel> regionMap = regionService.findMapByIds(new ArrayList<>(regionIds));
                Map<Integer, FriendModel> friendMap = friendService.findMapByUserId(SecurityUtil.getCurrentUserId());

                for(UserModel tmpUser : userModels) {
                    ChatroomUserDto roomUser = new ChatroomUserDto();
                    roomUser.setUserId(tmpUser.getId())
                            .setHeadImage(tmpUser.getHeadImage())
                            .setNickName(tmpUser.getNickName())
                            .setSex(tmpUser.getSex())
                            .setAgencyName(tmpUser.getAgencyName())
                            .setUserDesc(tmpUser.getUserDesc());

                    CountryModel tmpCountry = countryMap.get(tmpUser.getCountryId().intValue());
                    RegionModel tmpCity = regionMap.get(tmpUser.getCityId().intValue());
                    FriendModel tmpFriend = friendMap.get(tmpUser.getId().intValue());
                    ChatroomMember blockMember = blockMemberMap.get(String.valueOf(tmpUser.getId()));

                    if(tmpCountry != null) {
                        roomUser.setCountryCnName(tmpCountry.getCountryCnName());
                        roomUser.setCountryEnName(tmpCountry.getCountryEnName());
                        roomUser.setCountryFlag(tmpCountry.getCountryFlag());
                    }

                    if(tmpCity != null) {
                        roomUser.setCityCnName(tmpCity.getRegionCnName());
                        roomUser.setCityEnName(tmpCity.getRegionEnName());
                    }

                    if(tmpFriend != null) {
                        roomUser.setNoteName(tmpFriend.getNoteName());
                        roomUser.setIsFriend(1);
                    }

                    if(blockMember != null) {
                        roomUser.setIsBlock(1);
                    }

                    roomUsers.add(roomUser);
                }
            }

            return ResponseUtil.success(Collections.singletonMap("roomUsers", roomUsers));
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error();
        }
    }

    /**
     * 检查用户是否在聊天室
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkChatRoomUser", method = RequestMethod.POST)
    public Result checkChatRoomUser(@RequestBody QueryDto queryDto) {
        if(StringUtils.isEmpty(queryDto.getChatroomId()) || queryDto.getUserId() == null) {
            return ResponseUtil.error("房间ID或用户ID不能为空");
        }

        try {
            boolean isInChrm = rongyunUtil.checkChatRoomUser(queryDto.getChatroomId(), String.valueOf(queryDto.getUserId()));

            return ResponseUtil.success(Collections.singletonMap("isInChrm", isInChrm));
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error();
        }
    }

    /**
     * 修改用户是否解禁状态
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateUserBlock", method = RequestMethod.POST)
    public Result updateUserBlock(@RequestBody QueryDto queryDto) {
        if (StringUtils.isBlank(queryDto.getChatroomId()) || queryDto.getUserId() == null) {
            return ResponseUtil.error("聊天室ID和用户ID不能为空，请确定请求参数");
        }

        try {
            boolean flag = rongyunUtil.updateBlockUser(queryDto.getChatroomId(), String.valueOf(queryDto.getUserId()), queryDto.getBlockType());

            return ResponseUtil.success(Collections.singletonMap("blockType", queryDto.getBlockType()));
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error();
        }
    }
}
