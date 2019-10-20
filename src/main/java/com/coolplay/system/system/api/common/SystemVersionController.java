package com.coolplay.system.system.api.common;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.system.model.BannerModel;
import com.coolplay.system.system.model.SystemVersionModel;
import com.coolplay.system.system.service.ISystemVersionService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by majiancheng on 2019/10/18.
 */
@Controller
@RequestMapping("/api/common/systemVersion")
public class SystemVersionController {

    @Autowired
    private ISystemVersionService systemVersionService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map list(SystemVersionModel systemVersionModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {
        PageInfo<SystemVersionModel> pageInfo = systemVersionService
                .selectByFilterAndPage(systemVersionModel, pageNum, pageSize);

        return PageConvertUtil.grid(pageInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/getSystemVersion", method = RequestMethod.GET)
    public Result getSystemVersion(@RequestParam(value = "id") Integer id) {
        SystemVersionModel systemVersionModel = systemVersionService.findById(id);

        return ResponseUtil.success(systemVersionModel);
    }

    @ResponseBody
    @RequestMapping(value = "/updateSystemVersion", method = RequestMethod.POST)
    public Result updateSystemVersion(SystemVersionModel systemVersionModel) {
        if(systemVersionModel == null) {
            return ResponseUtil.error();
        }

        int updateCnt = systemVersionService.updateNotNull(systemVersionModel);
        return ResponseUtil.success();
    }
}
