package com.tokenbid.utils;

import com.tokenbid.controllers.AuctionController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Service class to send an email from "exceptions.tokenbid@gmail.com"
 */
@Service
public class EmailUtil {
    private static final Logger log = LogManager.getLogger(EmailUtil.class);
    private static final String from = "noreply@tokenbid.com";

    private static Logger logger = LogManager.getLogger(AuctionController.class.getName());

    @Autowired
    JavaMailSender emailSender;

    /**
     * Sending email to the user
     * @param to emailId Of receiver
     * @param subject subject of email
     * @param body body of email
     */
    public void sendEmail(String to, String subject, String body) {
        try {
            logger.debug("Sending email");
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            emailSender.send(message);
        } catch (MailException e) {
            log.error("Failed to send email");
            e.printStackTrace();
        }
    }

    /**
     * Sending email to the user
     * @param to emailId Of receiver
     * @param subject subject of email
     * @param body body of email
     */
    public void sendHTMLEmail(String to, String subject, String body) {
        try {
            logger.debug("Sending HTML Email");
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to create MimeMessage");
            e.printStackTrace();
        } catch (MailException e) {
            log.error("Failed to send email");
            e.printStackTrace();
        }
    }
}
