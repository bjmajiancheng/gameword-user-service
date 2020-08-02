package com.gameword.user.common.utils;

import com.gameword.user.common.dto.EmailDto;
import org.springframework.beans.factory.annotation.Value;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by majiancheng on 2020/3/22.
 */
public class MailSender implements Serializable {

    private static final long serialVersionUID = -197740278386677386L;

    @Value("${email.fromAddress}")
    private String fromAddress;

    @Value("${email.mailServerHost}")
    private String mailServerHost;

    @Value("${email.mailServerPort}")
    private String mailServerPort;

    @Value("${email.fromNickName}")
    private String fromNickName;

    @Value("${email.userName}")
    private String userName;

    @Value("${email.password}")
    private String password;

    /**
     * 以文本格式发送邮件
     *
     * @param mail
     * 待发送的邮件的信息
     * @throws GeneralSecurityException
     */
    public boolean sendTextMail(EmailDto mail) throws GeneralSecurityException {
        // 判断是否需要身份认证
        MailAuthenticator authenticator = null;
        Properties pro = mail.getProperties();
        if (mail.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MailAuthenticator(mail.getUserName(), mail.getPassword());
        }

        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mail.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mail.getToAddress());
            mailMessage.setRecipients(Message.RecipientType.TO, new Address[]{to});
            //创建邮件抄送者地址
            if(mail.getCCAddress()!=null) {
                //Address cc = new InternetAddress(mail.getCCAddress());
                mailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mail.getCCAddress()));
            }
            //创建邮件暗抄送者地址
            if(mail.getBCCAddress()!=null) {
                //Address bcc = new InternetAddress(mail.getBCCAddress());
                mailMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(mail.getBCCAddress()));
            }
            // 设置邮件消息的主题
            mailMessage.setSubject(mail.getSubject());
            // 设置邮件消息发送的时间s
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            String mailContent = mail.getContent();
            mailMessage.setText(mailContent);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 以HTML格式发送邮件
     *
     * @param mail
     *            待发送的邮件信息
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public boolean sendHtmlMail(EmailDto mail) throws GeneralSecurityException, UnsupportedEncodingException {
        // 判断是否需要身份认证
        MailAuthenticator authenticator = null;
        Properties props = mail.getProperties();
        // 如果需要身份认证，则创建一个密码验证器
        if (mail.isValidate()) {
            authenticator = new MailAuthenticator(mail.getUserName(), mail.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(props, authenticator);
        sendMailSession.setDebug(true);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mail.getFromAddress(),mail.getFromNickName()==null?"":mail.getFromNickName());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中,可以设置多个收件人，逗号隔开
            // Message.RecipientType.TO属性表示接收者的类型为TO,CC表示抄送,BCC暗送
            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getToAddress()));

            //创建邮件抄送者地址
            if(mail.getCCAddress()!=null) {
                //Address cc = new InternetAddress(mail.getCCAddress());
                mailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mail.getCCAddress()));
            }
            //创建邮件暗抄送者地址s
            if(mail.getBCCAddress()!=null) {
                //Address bcc = new InternetAddress(mail.getBCCAddress());
                mailMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(mail.getBCCAddress()));
            }
            // 设置邮件消息的主题
            mailMessage.setSubject(mail.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());

            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            MimeBodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(mail.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);

            // 设置信件的附件(用本地上的文件作为附件)
            FileDataSource fds=null;
            DataHandler dh=null;
            if (mail.getAttachments()!=null) {
                for (File file : mail.getAttachments()) {
                    html = new MimeBodyPart();
                    fds = new FileDataSource(file);
                    dh = new DataHandler(fds);
                    html.setFileName(file.getName());
                    html.setDataHandler(dh);
                    mainPart.addBodyPart(html);
                }
            }

            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            mailMessage.saveChanges();

            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public EmailDto generEmail(String toAddress, String subject, String content) {
        EmailDto mailInfo = new EmailDto();
        /*mailInfo.setCCAddress("xxxx@qq.com");*/
        mailInfo.setMailServerHost(this.getMailServerHost());
        mailInfo.setMailServerPort(this.getMailServerPort());
        mailInfo.setValidate(true);
        mailInfo.setFromNickName(this.getFromNickName());
        mailInfo.setUserName(this.getUserName()); // 实际发送者
        mailInfo.setPassword(this.getPassword());// 您的邮箱密码/授权码
        mailInfo.setFromAddress(this.getFromAddress()); // 设置发送人邮箱地址
        mailInfo.setToAddress(toAddress); // 设置接受者邮箱地址
        mailInfo.setSubject(subject);
        mailInfo.setContent(content);

        return mailInfo;
    }

    /**
     * 发送验证码
     * @param toAddress
     * @param subject
     * @param content
     */
    public Result sendEmail(String toAddress, String subject, String content) {

        try {
            boolean flag = this.sendTextMail(this.generEmail(toAddress, subject, content));

            return ResponseUtil.success("发送成功");
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("短信发送失败, 原因:" + e.getMessage());
        }
    }

    public static void main(String[] args) throws GeneralSecurityException, UnsupportedEncodingException {
        // 这个类主要是设置邮件
        EmailDto mailInfo = new EmailDto();
        //mailInfo.setCCAddress("igameword@163.com");
        mailInfo.setMailServerHost("smtp.163.com");
        mailInfo.setMailServerPort("465");
        mailInfo.setValidate(true);
        mailInfo.setFromNickName("GW");
        mailInfo.setUserName("igameword@163.com"); // 实际发送者
        mailInfo.setPassword("QOWSJHROYONKRKKF");// 您的邮箱密码/授权码
        mailInfo.setFromAddress("igameword@163.com"); // 设置发送人邮箱地址
        mailInfo.setToAddress("bjmajiancheng@163.com"); // 设置接受者邮箱地址
        mailInfo.setSubject("【镁锭项目计划回归】按照这次的来。反馈");
        mailInfo.setContent("已完成！反馈。这次一定行！<table><tr><td>第一行第一列</td><td>第二行第二列</td></tr></table>");

        // 这个类主要来发送邮件
        MailSender sms = new MailSender();
        //sms.sendTextMail(mailInfo); // 发送文体格式
        sms.sendHtmlMail(mailInfo); // 发送html格式,需要发送附件或者html（一般是table可用，链接好像不行）时，选择这个
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public String getFromNickName() {
        return fromNickName;
    }

    public void setFromNickName(String fromNickName) {
        this.fromNickName = fromNickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
