package com.coolplay.system.system.api.community;

import com.coolplay.system.common.utils.DateUtil;
import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.system.model.CirclePostModel;
import com.coolplay.system.system.model.CompanyModel;
import com.coolplay.system.system.model.PostModel;
import com.coolplay.system.system.model.UserModel;
import com.coolplay.system.system.service.*;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @Autowired
    private ICirclePostService circlePostService;

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
    @RequestMapping(value = "/updateIsTop", method = RequestMethod.POST)
    public Result updateIsTop(@RequestBody CirclePostModel circlePostModel) {
        PostModel postModel = new PostModel();
        postModel.setId(circlePostModel.getPostId());
        postModel.setIsTop(circlePostModel.getIsTop());

        int updateCnt = postService.updateNotNull(postModel);

        updateCnt = circlePostService.updateTopByPostId(circlePostModel.getPostId(), 0);
        if(CollectionUtils.isNotEmpty(circlePostModel.getCircleIds())) {
            updateCnt = circlePostService.updateTopByCirclePostInfo(circlePostModel.getCircleIds(), circlePostModel.getPostId(), circlePostModel.getIsTop());
        }

        return ResponseUtil.success();
    }

    /**
     * 全局置顶帖子
     *
     * @param postModel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/topPost", method = RequestMethod.POST)
    public Result topPost(@RequestBody PostModel postModel) {
        if(postModel.getIsTop() == 1) {
            postModel.setTopTime(new Date());
        } else {
            postModel.setTopTime(DateUtil.StringToDate("0000-00-00 00:00:00"));
        }
        /*PostModel postModel = new PostModel();
        postModel.setId(circlePostModel.getPostId());
        postModel.setIsTop(circlePostModel.getIsTop());*/

        int updateCnt = postService.updateNotNull(postModel);

        /*updateCnt = circlePostService.updateTopByPostId(circlePostModel.getPostId(), 0);
        if(CollectionUtils.isNotEmpty(circlePostModel.getCircleIds())) {
            updateCnt = circlePostService.updateTopByCirclePostInfo(circlePostModel.getCircleIds(), circlePostModel.getPostId(), circlePostModel.getIsTop());
        }*/

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

    @ResponseBody
    @RequestMapping(value = "/circleList", method = RequestMethod.GET)
    public Result circleList(@RequestParam("postId") Integer postId) {
        List<CirclePostModel> circlePosts = circlePostService.findByPostId(postId);

        return ResponseUtil.success(Collections.singletonMap("circlePosts", circlePosts));
    }
}
