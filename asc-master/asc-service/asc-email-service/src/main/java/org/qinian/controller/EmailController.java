package org.qinian.controller;

import org.qinian.domain.Result;
import org.qinian.domain.dto.email.SendEmailWithAttachmentDto;
import org.qinian.domain.dto.email.SendSimpleEmailDto;
import org.qinian.domain.dto.email.SendVerificationCodeDto;
import org.qinian.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/sendSimpleEmail")
    public Result sendSimpleEmail(@RequestBody SendSimpleEmailDto sendSimpleEmailDto) {
        emailService.sendSimpleEmail(sendSimpleEmailDto);
        return Result.success(200, "发送成功", null);
    }

    @PostMapping("/sendVerificationCode")
    public Result sendVerificationCode(@RequestBody SendVerificationCodeDto sendVerificationCodeDto) {
        emailService.sendVerificationCode(sendVerificationCodeDto);
        return Result.success(200, "发送成功", null);
    }

    @PostMapping("/sendEmailWithAttachment")
    public Result sendEmailWithAttachment(@RequestBody SendEmailWithAttachmentDto sendEmailWithAttachmentDto) throws Exception {
        emailService.sendEmailWithAttachment(sendEmailWithAttachmentDto);
        return Result.success(200, "发送成功", null);
    }
}
