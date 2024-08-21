package org.qinian.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.qinian.domain.dto.email.SendEmailWithAttachmentDto;
import org.qinian.domain.dto.email.SendSimpleEmailDto;
import org.qinian.domain.dto.email.SendVerificationCodeDto;
import org.qinian.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${mail.from}")
    private String fromEmail;

    /**
     * 发送简单邮件
     */
    public void sendSimpleEmail(SendSimpleEmailDto sendSimpleEmailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sendSimpleEmailDto.getTo());
        message.setSubject(sendSimpleEmailDto.getSubject());
        message.setText(sendSimpleEmailDto.getText());
        message.setFrom(fromEmail);

        mailSender.send(message);
    }

    /**
     * 发送验证码邮件
     */
    public void sendVerificationCode(SendVerificationCodeDto sendVerificationCodeDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sendVerificationCodeDto.getTo());
        message.setSubject(sendVerificationCodeDto.getSubject());
        message.setText("您的验证码是: " + sendVerificationCodeDto.getCode());
        message.setFrom(fromEmail);

        mailSender.send(message);
    }

    /**
     * 发送带附件的邮件
     *
     * @throws Exception 异常
     */
    public void sendEmailWithAttachment(SendEmailWithAttachmentDto sendEmailWithAttachmentDto) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(sendEmailWithAttachmentDto.getTo());
        helper.setSubject(sendEmailWithAttachmentDto.getSubject());
        helper.setText(sendEmailWithAttachmentDto.getText());
        helper.setFrom(fromEmail);

        helper.addAttachment(sendEmailWithAttachmentDto.getFile().getName(),
                sendEmailWithAttachmentDto.getFile());

        mailSender.send(mimeMessage);
    }
}
