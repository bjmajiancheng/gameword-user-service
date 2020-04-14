package com.gameword.user.user.api;

import com.gameword.user.common.dto.QueryDto;
import com.gameword.user.common.utils.PageConvertUtil;
import com.gameword.user.common.utils.ResponseUtil;
import com.gameword.user.common.utils.Result;
import com.gameword.user.user.model.HelpModel;
import com.gameword.user.user.service.IHelpService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by majiancheng on 2020/4/12.
 */
@Controller
@RequestMapping("/api/help")
public class HelpController {

    @Autowired
    private IHelpService helpService;

    /**
     * 帮助列表
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/helpList", method = RequestMethod.POST)
    public Result helpList(@RequestBody QueryDto queryDto) {
        try {
            HelpModel helpModel = new HelpModel();
            helpModel.setSort_("sort_asc");

            PageInfo<HelpModel> helpPage = helpService.selectByFilterAndPage(helpModel, queryDto.getPageNum(), queryDto.getPageSize());

            return ResponseUtil.success(PageConvertUtil.grid(helpPage));
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error();
        }
    }

    /**
     * 帮助详情
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/helpDetail", method = RequestMethod.POST)
    public Result helpDetail(@RequestBody QueryDto queryDto) {
        try {
            HelpModel helpModel = helpService.findById(queryDto.getId());

            return ResponseUtil.success(helpModel);
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error();
        }
    }
}
