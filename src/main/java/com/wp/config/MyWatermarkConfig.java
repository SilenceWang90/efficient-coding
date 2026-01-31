package com.wp.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.easywatermark.core.config.WatermarkConfig;
import org.easywatermark.enums.CenterLocationTypeEnum;
import org.easywatermark.enums.DiagonalDirectionTypeEnum;
import org.easywatermark.enums.OverspreadTypeEnum;

import java.awt.*;

/**
 * @author wangpeng
 * @description 水印配置
 * @date 2026/1/31 15:19
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyWatermarkConfig extends WatermarkConfig {
    // 水印颜色
    private Color color = Color.BLACK;
    // 忽略旋转，仍在开发
    private boolean ignoreRotation = true;
    // 水印透明度
    private float alpha = 1;
    // 铺满水印子类型
    private OverspreadTypeEnum overspreadType = OverspreadTypeEnum.NORMAL;
    // 居中水印子类型
    private CenterLocationTypeEnum centerLocationType = CenterLocationTypeEnum.VERTICAL_CENTER;
    // 对角水印子类型
    private DiagonalDirectionTypeEnum diagonalDirectionType = DiagonalDirectionTypeEnum.TOP_TO_BOTTOM;

    /**
     * An angle, in degrees
     * The angle of clockwise rotation
     */
    private float angle = 0;
}
