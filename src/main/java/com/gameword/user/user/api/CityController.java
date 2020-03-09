package com.gameword.user.user.api;

import com.gameword.user.common.dto.QueryDto;
import com.gameword.user.common.utils.ResponseUtil;
import com.gameword.user.common.utils.Result;
import com.gameword.user.core.model.UserModel;
import com.gameword.user.security.constants.SecurityConstant;
import com.gameword.user.security.utils.SecurityUtil;
import com.gameword.user.user.model.CityModel;
import com.gameword.user.user.model.UserPassMappingModel;
import com.gameword.user.user.service.ICityService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by majiancheng on 2020/3/9.
 */
@Controller
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private ICityService cityService;

    /**
     * 城市列表
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result list(@RequestBody QueryDto queryDto) {
        try {
            List<CityModel> cityModels = cityService.selectByFilter(new CityModel());

            return ResponseUtil.success(Collections.singletonMap("citys", cityModels));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

}
