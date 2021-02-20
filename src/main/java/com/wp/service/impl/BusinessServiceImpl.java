package com.wp.service.impl;

import com.wp.dto.ReadExcelDto;
import com.wp.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Classname BusinessServiceImpl
 * @Description 业务服务实现
 * @Date 2021/2/20 10:17
 * @Created by wangpeng116
 */
@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {
    @Override
    public void dealBusiness(ReadExcelDto readExcelDto) {
        log.info("业务处理");
    }
}
