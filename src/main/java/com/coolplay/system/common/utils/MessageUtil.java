package com.coolplay.system.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by majiancheng on 2019/10/5.
 */
public class MessageUtil {

    /**
     * 发送短信信息
     *
     * @param mobile
     * @param message
     * @return
     */
    public static Result sendMessage(String mobile, String message) {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://192.168.224.182:8088/sapPushExpenseWebservice?wsdl");
        try {

            List<String> list = new ArrayList<>();
            list.add("测试");

            Object[] objects = client.invoke("pushExpense", list);
            System.out.println(JSON.toJSONString(objects[0]));

        }catch (Exception e){
            e.printStackTrace();
        }

        return ResponseUtil.success();
    }

}
