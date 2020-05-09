package com.gameword.user.user.api;

import com.gameword.user.common.dto.QueryDto;
import com.gameword.user.common.utils.PageConvertUtil;
import com.gameword.user.common.utils.ResponseUtil;
import com.gameword.user.common.utils.Result;
import com.gameword.user.common.utils.RongyunUtil;
import com.gameword.user.core.model.UserModel;
import com.gameword.user.security.service.IUserService;
import io.rong.models.chatroom.ChatroomMember;
import io.rong.models.response.ChatroomUserQueryResult;
import io.rong.models.response.ResponseResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /**
     * 创建聊天室
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createChatroom", method = RequestMethod.POST)
    public Result createChatroom(@RequestBody QueryDto queryDto) {
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
        try {
            ChatroomUserQueryResult result = rongyunUtil.chatroomUsers(queryDto.getChatroomId());
            List<ChatroomMember> chatroomMembers = result.getMembers();

            List<UserModel> userModels = new ArrayList<UserModel>();
            if(CollectionUtils.isNotEmpty(chatroomMembers)) {
                List<Integer> userIds = new ArrayList<Integer>();
                for(ChatroomMember chatroomMember : chatroomMembers) {
                    userIds.add(Integer.valueOf(chatroomMember.getId()));
                }

                userModels = userService.findByUserIds(userIds);
            }

            return ResponseUtil.success(Collections.singletonMap("userModels", userModels));
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
        try {
            boolean isInChrm = rongyunUtil.checkChatRoomUser(queryDto.getChatroomId(), String.valueOf(queryDto.getUserId()));

            return ResponseUtil.success(Collections.singletonMap("isInChrm", isInChrm));
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error();
        }
    }
}
