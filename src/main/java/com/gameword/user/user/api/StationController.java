package com.gameword.user.user.api;

import com.gameword.user.user.service.IStationDetailService;
import com.gameword.user.user.service.IStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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


}
