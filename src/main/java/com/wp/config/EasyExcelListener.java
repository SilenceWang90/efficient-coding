package com.wp.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wp.dto.ReadExcelDto;
import com.wp.mapper.SaveMapper;
import com.wp.service.BusinessService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Classname EasyExcelListener
 * @Description EasyExcel读取文件监听器：不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 * 比如存库需要mapper，业务处理需要service，那么就需要构造方法从上层传入
 * @Date 2021/2/20 10:13
 * @Created by wangpeng116
 */
@Slf4j
public class EasyExcelListener extends AnalysisEventListener<ReadExcelDto> {

    private BusinessService businessService;
    private SaveMapper saveMapper;
    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 每隔3000条存储数据库，实际使用中可以更多条，根据服务器内存以及数据库的吞吐量来设计合理的大小。使用结束后清理list，防止内存溢出
     */
    private static final int BATCH_COUNT = 3000;

    /**
     * 要处理数据的集合，通过BATCH_COUNT限制内存大小
     */
    private List<ReadExcelDto> dataList = Lists.newArrayList();

    /**
     * 由于不能被Spring管理，因此用到的其他对象需要通过构造函数传入使用
     *
     * @param businessService 业务service
     * @param saveMapper      保存的mapper
     */
    public EasyExcelListener(BusinessService businessService, SaveMapper saveMapper) {
        this.businessService = businessService;
        this.saveMapper = saveMapper;
    }

    /**
     * 每一行数据解析都会调用此方法，如果要从指定行开始处理的话可以在此方法中定义全局变量进行读取，EasyExcel默认从第二行读取数据！！！
     *
     * @param readExcelDto
     * @param analysisContext
     */
    @Override
    public void invoke(ReadExcelDto readExcelDto, AnalysisContext analysisContext) {
        try {
            String value = objectMapper.writeValueAsString(readExcelDto);
            log.info("解析到一条数据：{}", value);
            // 数据处理
            businessService.dealBusiness(readExcelDto);
            dataList.add(readExcelDto);
            // 数据量达标进行批量保存
            if (dataList.size() >= BATCH_COUNT) {
                saveMapper.saveData(dataList);
                // 数据处理完成立即清空
                dataList.clear();
            }
        } catch (Exception e) {
            log.error("读取数据异常：{}", e);
        }

    }

    /**
     * 所有数据处理完了就会调用该方法
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 一定要保存哦，因为上面的逻辑可能会造成最后一部分数据未保存(因为dataList的数量不足BATCH_COUNT)，所以这里是为了保存其余的数据！！！
        saveMapper.saveData(dataList);
        log.info("所有数据解析完成");
    }
}
