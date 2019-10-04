package com.coolplay.system.system.api.community;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.system.model.CompanyModel;
import com.coolplay.system.system.model.PostModel;
import com.coolplay.system.system.model.UserModel;
import com.coolplay.system.system.service.IAppUserService;
import com.coolplay.system.system.service.IPostCommentService;
import com.coolplay.system.system.service.IPostLabelService;
import com.coolplay.system.system.service.IPostService;
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
@RequestMapping("/api/community/post")
public class PostController {

    @Autowired
    private IPostService postService;

    @Autowired
    private IPostCommentService postCommentService;

    @Autowired
    private IPostLabelService postLabelService;

    @Autowired
    private IAppUserService appUserService;

    @ResponseBody
    @RequestMapping(value="/list", method = RequestMethod.GET)
    public Map list(PostModel postModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {
        postModel.setIsDel(0);

        PageInfo<PostModel> pageInfo = postService.selectByFilterAndPage(postModel, pageNum, pageSize);

        if(CollectionUtils.isNotEmpty(pageInfo.getList())) {
            List<Integer> userIds = new ArrayList<Integer>();
            for(PostModel tmpPostModel : pageInfo.getList()) {
                if(!userIds.contains(tmpPostModel.getUserId())) {
                    userIds.add(tmpPostModel.getUserId());
                }
            }

            Map<Integer, UserModel> userModelMap = appUserService.findMapByUserIds(userIds);
            for(PostModel tmpPostModel : pageInfo.getList()) {
                UserModel userModel = userModelMap.get(tmpPostModel.getUserId());

                if(userModel != null) {
                    tmpPostModel.setPublicUserName(userModel.getUserName());
                }
            }

        }

        return PageConvertUtil.grid(pageInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/showPost", method = RequestMethod.GET)
    public Result showPost(@RequestParam("id") Integer id) {
        PostModel postModel = postService.findById(id);

        UserModel userModel = appUserService.findById(postModel.getUserId());
        if(userModel != null) {
            postModel.setPublicUserName(userModel.getUserName());
        }

        return ResponseUtil.success(postModel);
    }

    @ResponseBody
    @RequestMapping(value = "/updateIsTop", method = RequestMethod.GET)
    public Result updateIsTop(@RequestParam("id") Integer id, @RequestParam("isTop") Integer isTop) {
        PostModel postModel = new PostModel();
        postModel.setId(id);
        postModel.setIsTop(isTop);

        int updateCnt = postService.updateNotNull(postModel);

        return ResponseUtil.success();
    }

    @ResponseBody
    @RequestMapping(value = "/delPost", method = RequestMethod.GET)
    public Result delPost(@RequestParam("id") Integer id) {
        PostModel postModel = new PostModel();
        postModel.setId(id);
        postModel.setIsDel(1);

        int updateCnt = postService.updateNotNull(postModel);

        return ResponseUtil.success();
    }
}
