package com.coolplay.system.system.api.community;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.system.model.CircleModel;
import com.coolplay.system.system.model.PostModel;
import com.coolplay.system.system.model.UserModel;
import com.coolplay.system.system.service.IAppUserService;
import com.coolplay.system.system.service.ICircleService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by majiancheng on 2019/10/4.
 */
@Controller
@RequestMapping("/api/community/circle")
public class CircleController {

    @Autowired
    private ICircleService circleService;

    @Autowired
    private IAppUserService appUserService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map list(CircleModel circleModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {

        PageInfo<CircleModel> pageInfo = circleService.selectByFilterAndPage(circleModel, pageNum, pageSize);

        if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
            List<Integer> userIds = new ArrayList<Integer>();
            for (CircleModel tmpCircleModel : pageInfo.getList()) {
                if (!userIds.contains(tmpCircleModel.getUserId())) {
                    userIds.add(tmpCircleModel.getUserId());
                }
            }

            Map<Integer, UserModel> userModelMap = appUserService.findMapByUserIds(userIds);
            for (CircleModel tmpCircleModel : pageInfo.getList()) {
                UserModel userModel = userModelMap.get(tmpCircleModel.getUserId());
                if (userModel != null) {
                    tmpCircleModel.setUserName(userModel.getUserName());
                }
            }
        }

        return PageConvertUtil.grid(pageInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/reviewCircle", method = RequestMethod.GET)
    public Result reviewCircle(@RequestParam("id") Integer id, @RequestParam("status") Integer status, @RequestParam("rejectReason")String rejectReason) {
        CircleModel circleModel = new CircleModel();
        circleModel.setId(id);
        circleModel.setStatus(status);
        circleModel.setRejectReason(rejectReason);
        circleModel.setReviewTime(new Date());
        circleModel.setReviewStatus(1);

        int updateCnt = circleService.updateNotNull(circleModel);

        return ResponseUtil.success();
    }

    @ResponseBody
    @RequestMapping(value = "/getCircle", method = RequestMethod.GET)
    public Result getCircle(@RequestParam("id") Integer id) {
        CircleModel circleModel = circleService.findById(id);

        if(circleModel != null) {
            UserModel userModel = appUserService.findById(circleModel.getUserId());
            if(userModel != null) {
                circleModel.setUserName(userModel.getUserName());
            }
        }

        return ResponseUtil.success(circleModel);
    }

}
