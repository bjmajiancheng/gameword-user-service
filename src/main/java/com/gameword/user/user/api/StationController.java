package com.gameword.user.user.api;

import com.gameword.user.common.dto.QueryDto;
import com.gameword.user.common.utils.ResponseUtil;
import com.gameword.user.common.utils.Result;
import com.gameword.user.user.model.StationDetailModel;
import com.gameword.user.user.model.StationModel;
import com.gameword.user.user.service.IStationDetailService;
import com.gameword.user.user.service.IStationService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

/**
 * Created by majiancheng on 2020/3/9.
 */
@Controller
@RequestMapping("/api/station")
public class StationController {

    @Autowired
    private IStationService stationService;

    @Autowired
    private IStationDetailService stationDetailService;

    /**
     * 城市驿站信息
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Result detail(@RequestBody QueryDto queryDto) {

        try {
            StationModel stationModel = stationService.findByCityId(queryDto.getCityId());

            if(stationModel == null) {
                return ResponseUtil.error("暂无城市相关信息, 请选择其他城市");
            }

            StationDetailModel stationDetailModel = new StationDetailModel();
            if(queryDto.getLanguage() == 1) {
                stationDetailModel.setCityInfo(stationModel.getCnCityInfo());
                stationDetailModel.setBusinessCooperation(stationModel.getCnBusinessCooperation());
            } else if(queryDto.getLanguage() == 2) {
                stationDetailModel.setCityInfo(stationModel.getEnCityInfo());
                stationDetailModel.setBusinessCooperation(stationModel.getEnBusinessCooperation());
            }

            return ResponseUtil.success(Collections.singletonMap("stationDetail", stationDetailModel));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

}
