package org.qinian.model;

import org.qinian.domain.Result;
import org.qinian.domain.dto.email.SendEmailWithAttachmentDto;
import org.qinian.domain.dto.email.SendSimpleEmailDto;
import org.qinian.domain.dto.email.SendVerificationCodeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("email-service")
public interface RemoteEmailService {
    @PostMapping("/email/sendSimpleEmail")
    Result sendSimpleEmail(@RequestBody SendSimpleEmailDto sendSimpleEmailDto);

    @PostMapping("/email/sendVerificationCode")
    Result sendVerificationCode(@RequestBody SendVerificationCodeDto sendVerificationCodeDto);

    @PostMapping("/email/sendEmailWithAttachment")
    Result sendEmailWithAttachment(@RequestBody SendEmailWithAttachmentDto sendEmailWithAttachmentDto);
}
