package com.coolplay.system.common.api;

import com.coolplay.system.common.tools.RedisCache;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by majiancheng on 2019/9/17.
 */
@Controller
@RequestMapping("/api/noneAuth")
public class NoneAuthController {

    @Autowired
    private Producer producer;

    @Autowired
    private RedisCache redisCache;

    @RequestMapping("/captcha")
    public void captcha(@RequestParam("vkey") String vkey, HttpServletRequest httpServletRequest,
            HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);

        redisCache.set(vkey, text, 120);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }
}
