package org.qinian.domain.dto.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailWithAttachmentDto {
    /**
     * String to, String subject, String text, File file
     */

    private String to;
    private String subject;
    private String text;
    private File file;
}
