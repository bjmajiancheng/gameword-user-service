package com.coolplay.system.system.api.community;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.system.model.PostCommentModel;
import com.coolplay.system.system.model.PostModel;
import com.coolplay.system.system.model.UserModel;
import com.coolplay.system.system.service.IAppUserService;
import com.coolplay.system.system.service.IPostCommentService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by majiancheng on 2019/10/3.
 */
@Controller
@RequestMapping("/api/community/postComment")
public class PostCommentController {

    @Autowired
    private IPostCommentService postCommentService;

    @Autowired
    private IAppUserService appUserService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map list(PostCommentModel postCommentModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {

        postCommentModel.setIsDel(0);
        PageInfo<PostCommentModel> pageInfo = postCommentService
                .selectByFilterAndPage(postCommentModel, pageNum, pageSize);
        if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
            List<Integer> userIds = new ArrayList<Integer>();
            for (PostCommentModel tmpPostComment : pageInfo.getList()) {
                if (!userIds.contains(tmpPostComment.getCommentUserId())) {
                    userIds.add(tmpPostComment.getCommentUserId());
                }
            }

            Map<Integer, UserModel> userModelMap = appUserService.findMapByUserIds(userIds);
            for(PostCommentModel tmpPostComment : pageInfo.getList()) {
                UserModel userModel = userModelMap.get(tmpPostComment.getCommentUserId());

                if(userModel != null) {
                    tmpPostComment.setCommentUserName(userModel.getUserName());
                }
            }
        }

        return PageConvertUtil.grid(pageInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/delComment", method = RequestMethod.GET)
    public Result delComment(@RequestParam("id") Integer id) {
        PostCommentModel postCommentModel = new PostCommentModel();
        postCommentModel.setId(id);
        postCommentModel.setIsDel(1);

        int updateCnt = postCommentService.updateNotNull(postCommentModel);

        return ResponseUtil.success();
    }
}
