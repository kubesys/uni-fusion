
package com.qnkj.common.utils.Excel;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel注解定义
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

	//定义字段导出水平对齐样式，默认居中
	HorizontalAlignment align() default HorizontalAlignment.CENTER;

}
