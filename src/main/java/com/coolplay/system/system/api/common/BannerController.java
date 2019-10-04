package com.coolplay.system.system.api.common;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.security.security.SecurityUser;
import com.coolplay.system.security.utils.SecurityUtil;
import com.coolplay.system.system.model.BannerModel;
import com.coolplay.system.system.model.CircleModel;
import com.coolplay.system.system.model.UserModel;
import com.coolplay.system.system.service.IBannerService;
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
@RequestMapping("/api/common/banner")
public class BannerController {

    @Autowired
    private IBannerService bannerService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map list(BannerModel bannerModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {
        bannerModel.setIsDel(0);
        PageInfo<BannerModel> pageInfo = bannerService.selectByFilterAndPage(bannerModel, pageNum, pageSize);

        return PageConvertUtil.grid(pageInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/delBanner", method = RequestMethod.GET)
    public Result delBanner(@RequestParam("id")Integer id) {
        BannerModel bannerModel = new BannerModel();
        bannerModel.setId(id);
        bannerModel.setIsDel(1);
        bannerService.updateNotNull(bannerModel);

        return ResponseUtil.success();
    }

    @ResponseBody
    @RequestMapping(value = "/saveBanner", method = RequestMethod.POST)
    public Result saveBanner(BannerModel bannerModel) {
        SecurityUser securityUser = SecurityUtil.getCurrentSecurityUser();
        bannerModel.setSystemUserId(securityUser.getId());
        bannerModel.setIsDel(0);
        bannerModel.setCtime(new Date());

        int saveCnt = bannerService.saveNotNull(bannerModel);

        return ResponseUtil.success();
    }

}
