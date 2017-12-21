package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${mail.from}")
    private String sourceAddress;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(String recipient, String text) {
        try {
            javaMailSender.send(createMimeMessage(recipient, text));
        } catch (MessagingException e) {
            log.warn("Email for {} couldn't be sent due to incorrect message parameter", recipient);
        }
    }

    private MimeMessage createMimeMessage(String recipient, String text) throws MessagingException {
        log.debug("Start to create email message");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage);
        mailMessage.setTo(recipient);
        mailMessage.setFrom(sourceAddress);
        mailMessage.setSubject("Report Info");
        mailMessage.setText(text);

        log.debug("Finish to create email message");
        return mimeMessage;
    }
}