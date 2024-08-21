package org.qinian.domain.dto.alert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qinian.enums.AlertLevels;
import org.qinian.enums.AlertTypes;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddAlertInfoDto {
    private Long fishRaftId;

    private AlertTypes alertTypeId;

    private AlertLevels alertLevel;

    private String alertedMessage;
}
