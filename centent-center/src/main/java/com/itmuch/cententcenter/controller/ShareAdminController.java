package com.itmuch.cententcenter.controller;

import com.itmuch.cententcenter.DTO.Centent.ShareAuditDTO;
import com.itmuch.cententcenter.auth.CheckAuthorization;
import com.itmuch.cententcenter.entity.Share;
import com.itmuch.cententcenter.service.impl.ShareServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/share")
public class ShareAdminController {
    @Autowired
    private ShareServiceImpl shareService;
    @CheckAuthorization("admin")
    @PutMapping("/audit/{id}")
    public Share auditById(@PathVariable("id") Integer id, @RequestBody ShareAuditDTO shareAuditDTO) {
        return shareService.auditById(id, shareAuditDTO);
    }
}
