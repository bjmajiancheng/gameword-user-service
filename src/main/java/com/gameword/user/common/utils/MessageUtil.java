package com.gameword.user.common.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * Created by majiancheng on 2019/10/5.
 */
public class MessageUtil implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(MessageUtil.class);

    private String userId;

    private String account;

    private String password;

    private String uri;

    private Properties smsProperties;

    /**
     * 加载配置
     */
    public void loadProperty() {
        String path = MessageUtil.class.getResource("").getPath();
        path = path.split("classes/com")[0] + "classes";
        smsProperties = new Properties();
        String smsFilePath = path + "/properties/message.properties";
        try {
            smsProperties.load(new InputStreamReader(
                    new FileInputStream(smsFilePath), "UTF-8"));
        } catch (IOException e) {
            logger.error(String.format("message.properties not find %s", smsFilePath), e);
        }

    }

    public String getProperty(String key, String[] values) {
        String msgContent = smsProperties.getProperty(key);

        if(StringUtils.isEmpty(msgContent)) {
            return "";
        }

        if(values == null || values.length == 0) {
            return msgContent;
        } else {
            return MessageFormat.format(msgContent, values);
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * 发送短信信息
     *
     * @param mobile
     * @param key
     * @param values
     * @return
     */
    public Result sendMessage(String mobile, String key, String[] values) {

        try{
            /*Map<String, String> params = new HashMap<String, String>();
            params.put("UserID", this.getUserId());
            params.put("Account", this.getAccount());
            params.put("Password", this.getPassword());
            params.put("Phones", String.format("%s;", mobile));
            params.put("SendTime", "");
            params.put("SendType", "1");
            params.put("PostFixNumber", "");

            String content = this.getProperty(key, values);
            if(StringUtils.isEmpty(content)) {
                return ResponseUtil.error("短信模板不存在");
            }
            params.put("Content", content);*/


            /*HttpClientResult result = HttpClientUtil.doGet(this.getUri(), params);
            logger.info("手机号码:{}, 短信发送结果:{}", mobile, JSON.toJSONString(result));*/

            String content = this.getProperty(key, values);
            if(StringUtils.isEmpty(content)) {
                return ResponseUtil.error("短信模板不存在");
            }
            Integer result = this.sendSMSPost(mobile, content);


            return ResponseUtil.success(result);
        } catch(Exception e) {
            e.printStackTrace();
        }


        return ResponseUtil.success();
    }

    public static void main(String[] args) {
        try {
            /*Map<String, String> params = new HashMap<String, String>();
            params.put("CorpID", "BJJS009510");
            params.put("Pwd", "sywh123");
            params.put("Mobile", String.format("%s", "13717689765"));
            params.put("SendTime", "");
            params.put("Cell", "");

            String content = "【素娱】您的短信验证码是4648.有效期180秒，如非本人操作，请忽略该短信。";
            content = URLEncoder.encode(content, "GBK");
            params.put("Content", content);
            System.out.println(params.get("Content"));

            HttpClientResult result = HttpClientUtil.doPost("https://sdk2.028lk.com/sdk2/BatchSend2.aspx", params);
            logger.info("手机号码:{}, 短信发送结果:{}", "13717689765", JSON.toJSONString(result));*/

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 短信发送接口
     *
     * @param mobile
     * @param content
     * @return
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    public int sendSMSPost(String mobile, String content) throws MalformedURLException, UnsupportedEncodingException {

        String inputLine = "";
        int value = -2;

        String CorpID = this.getAccount();
        String Pwd = this.getPassword();
        String sendContent = URLEncoder.encode(content, "GBK");

        String strUrl = this.getUri();
        String param = "CorpID=" + CorpID + "&Pwd=" + Pwd + "&Mobile=" + mobile
                + "&Content=" + sendContent + "&Cell=&SendTime=";

        try {
            inputLine = sendPost(strUrl, param);
            System.out.println("发送短信成功" + mobile);
            value = new Integer(inputLine).intValue();
        } catch (Exception e) {
            System.out.println("发送短信异常");
            value = -2;
        }
        System.out.println(String.format("发送结果:%d", value));

        return value;
    }

    /**
     * POST请求
     * @param url
     * @param param
     * @return
     */
    public String sendPost(String url, String param) {

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("POST 请求异常" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}
