package com.batman.bysj.common.mail.batmanbysjcommonmail;

import org.slf4j.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.StringUtils.isEmpty;

public class MailClient {

    private static final String TRANSPORT = "smtp";

    private static final String META = "<meta http-equiv=Context-Type context=CONTENT_TYPE>";

    private final Logger logger = getLogger(getClass());

    private String username;

    private String password;

    private Properties properties;

    public String getUsername() {
        return username;
    }

    public MailClient setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MailClient setPassword(String password) {
        this.password = password;
        return this;
    }

    public Properties getProperties() {
        return properties;
    }

    public MailClient setProperties(Properties properties) {
        this.properties = properties;
        return this;
    }

    public void send(Mail mail) throws MessagingException {

        Objects.requireNonNull(mail);

        String username = getUsername();
        String password = getPassword();
        String sender = mail.getSender();

        if (isEmpty(sender)) {
            sender = username;
        }

        logger.debug("发送人: {}", sender);

        String contentType = mail.getContentType();
        String content = META.replace("CONTENT_TYPE", contentType);

        content += mail.getContent();

        logger.debug("邮件类型: {}", contentType);
        logger.debug("邮件内容: {}", content);

        BodyPart body = new MimeBodyPart();
        body.setContent(content, contentType);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(body);

        //添加附件
        List<String> attachmentPaths = mail.getAttachmentPaths();
        if (attachmentPaths != null && !attachmentPaths.isEmpty()) {
            for (String file : attachmentPaths) {
                BodyPart bodyPart = new MimeBodyPart();
                FileDataSource fileDataSource = new FileDataSource(file);
                bodyPart.setDataHandler(new DataHandler(fileDataSource));
                bodyPart.setFileName(fileDataSource.getName());
                multipart.addBodyPart(bodyPart);
            }
        }

        // 根据配置创建 Session
        Session session = Session.getInstance(getProperties());

        // 转换收件人地址
        List<String> receivers = mail.getReceivers();
        InternetAddress[] addresses = new InternetAddress[receivers.size()];

        for (int i = 0; i < addresses.length; i++) {
            String add = receivers.get(i);
            logger.debug("收件人 [{}]: {}", i, add);
            addresses[i] = new InternetAddress(add);
        }

        // 创建邮件主体
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setHeader("X-Priority", mail.getPriority().value());
        mimeMessage.setFrom(new InternetAddress(sender));
        mimeMessage.setRecipients(Message.RecipientType.TO, addresses);
        mimeMessage.setSubject(mail.getSubject());
        mimeMessage.setContent(multipart);
        mimeMessage.saveChanges();

        // 发送
        Transport transport = session.getTransport(TRANSPORT);

        try {
            if (!isEmpty(password)) {

                logger.debug("使用用户名/密码连接: {} / {}", username, password);
                transport.connect(username, password);
            } else {
                logger.debug("无认证信息连接");
                transport.connect();
            }

            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

            logger.info("邮件已发送: {} [{}] => {}", sender, mail.getSubject(), receivers);

        } finally {
            try {
                transport.close();
            } catch (Exception e) {
                logger.error("关闭邮件连接出现异常", e);
            }
        }
    }
}
