package com.coolplay.system.system.api.community;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.system.model.*;
import com.coolplay.system.system.service.*;
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
 * Created by majiancheng on 2019/10/4.
 */
@Controller
@RequestMapping("/api/community/companyCircle")
public class CompanyCricleController {

    @Autowired
    private ICompanyCircleService companyCircleService;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private ICircleService circleService;

    @Autowired
    private IAppUserService appUserService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map list(CompanyCircleModel companyCircleModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {

        PageInfo<CompanyCircleModel> pageInfo = companyCircleService
                .selectByFilterAndPage(companyCircleModel, pageNum, pageSize);

        if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
            List<Integer> companyIds = new ArrayList<Integer>();
            List<Integer> circleIds = new ArrayList<Integer>();

            for(CompanyCircleModel tmpCompanyCircle : pageInfo.getList()) {
                if(!companyIds.contains(tmpCompanyCircle.getCompanyId())) {
                    companyIds.add(tmpCompanyCircle.getCompanyId());
                }

                if(!circleIds.contains(tmpCompanyCircle.getCircleId())) {
                    circleIds.add(tmpCompanyCircle.getCircleId());
                }
            }

            Map<Integer, CompanyModel> companyModelMap = companyService.findMapByIds(companyIds);
            Map<Integer, CircleModel> circleModelMap = circleService.findMapByIds(circleIds);
            for(CompanyCircleModel tmpCompanyCircle : pageInfo.getList()) {
                CompanyModel companyModel = companyModelMap.get(tmpCompanyCircle.getCompanyId());
                CircleModel circleModel = circleModelMap.get(tmpCompanyCircle.getCircleId());
                if(companyModel != null) {
                    tmpCompanyCircle.setCompanyName(companyModel.getCompanyName());
                }
                if(circleModel != null) {
                    tmpCompanyCircle.setCircleName(circleModel.getCircleName());
                    tmpCompanyCircle.setCircleCtime(circleModel.getCtime());
                    tmpCompanyCircle.setMemberCnt(circleModel.getMemberCnt());
                    tmpCompanyCircle.setCircleDesc(circleModel.getCircleDesc());

                    UserModel userModel = appUserService.findById(circleModel.getUserId());
                    if(userModel != null) {
                        tmpCompanyCircle.setUserName(userModel.getUserName());
                    }
                }
            }
        }

        return PageConvertUtil.grid(pageInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/updateCompanyCircleDisable", method = RequestMethod.GET)
    public Result disableCompanyCircle(@RequestParam("id")Integer id, @RequestParam("isDisable")Integer isDisable, @RequestParam("disableReason") String disableReason) {
        CompanyCircleModel companyCircleModel = new CompanyCircleModel();
        companyCircleModel.setId(id);
        companyCircleModel.setIsDisable(isDisable);
        companyCircleModel.setDisableReason(disableReason);

        int updateCnt = companyCircleService.updateNotNull(companyCircleModel);
        return ResponseUtil.success();
    }
}
