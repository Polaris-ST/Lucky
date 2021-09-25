package com.itmuch.cententcenter;

import com.itmuch.cententcenter.DTO.Centent.ShareAuditDTO;
import com.itmuch.cententcenter.DTO.Centent.ShareDTO;
import com.itmuch.cententcenter.DTO.enums.AuditStatusEnum;
import com.itmuch.cententcenter.entity.Share;
import com.itmuch.cententcenter.mapper.RocketmqTransactionLogMapper;
import com.itmuch.cententcenter.mapper.ShareMapper;
import com.itmuch.cententcenter.service.impl.ShareServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CententCenterApplicationTests {
    @Autowired
    ShareServiceImpl shareService;
    @Autowired
    ShareMapper shareMapper;

    @Test
    void contextLoads() {
        shareMapper.updateByIdSelective(Share.builder().id(1).reason("wwww").auditStatus("PASS").build());
    }

}
