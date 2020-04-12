package com.gameword.user.user.api;

import com.gameword.user.common.dto.QueryDto;
import com.gameword.user.common.utils.PageConvertUtil;
import com.gameword.user.common.utils.ResponseUtil;
import com.gameword.user.common.utils.Result;
import com.gameword.user.security.utils.SecurityUtil;
import com.gameword.user.user.model.FeedbackModel;
import com.gameword.user.user.model.HelpModel;
import com.gameword.user.user.service.IFeedbackService;
import com.gameword.user.user.service.IHelpService;
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
@Controller("/api/feedback")
public class FeedbackController {

    @Autowired
    private IFeedbackService feedbackService;

    /**
     * 提交反馈信息
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveFeedback", method = RequestMethod.POST)
    public Result saveFeedback(@RequestBody QueryDto queryDto) {
        try {
            FeedbackModel feedbackModel = new FeedbackModel();
            feedbackModel.setUserId(SecurityUtil.getCurrentUserId());
            feedbackModel.setContent(queryDto.getContent());

            int saveCnt = feedbackService.saveNotNull(feedbackModel);

            return ResponseUtil.success();
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error();
        }
    }


}
