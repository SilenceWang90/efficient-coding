package com.wp.service;

import com.wp.dto.ReadExcelDto;

/**
 * @Classname BusinessService
 * @Description 业务服务
 * @Date 2021/2/20 10:17
 * @Created by wangpeng116
 */
public interface BusinessService {
    /**
     * 业务逻辑处理
     * @param readExcelDto
     */
    void dealBusiness(ReadExcelDto readExcelDto);
}
