package com.itmuch.cententcenter.DTO.Centent;

import com.itmuch.cententcenter.DTO.enums.AuditStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ShareAuditDTO {
    //审核状态
    private AuditStatusEnum auditStatusEnum;
    //原因
    private String reason;
}
