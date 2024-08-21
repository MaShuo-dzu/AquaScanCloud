package org.qinian.service;

import org.qinian.domain.dto.email.SendEmailWithAttachmentDto;
import org.qinian.domain.dto.email.SendSimpleEmailDto;
import org.qinian.domain.dto.email.SendVerificationCodeDto;

public interface EmailService {
    public void sendSimpleEmail(SendSimpleEmailDto sendSimpleEmailDto);

    public void sendVerificationCode(SendVerificationCodeDto sendVerificationCodeDto);

    public void sendEmailWithAttachment(SendEmailWithAttachmentDto sendEmailWithAttachmentDto) throws Exception;
}
