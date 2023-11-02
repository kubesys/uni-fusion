package com.qnkj.common.entitys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InvoicePrintInitInfo extends BaseRecordConfig {

	@ApiModelProperty("模块名")
	public String printmodule = "";

	@ApiModelProperty("单据打印名称")
	public String printname = "";

	@ApiModelProperty("单据打印英文名称")
	public String printkeyname = "";

	@ApiModelProperty("启用")
	public String status = "";

	@ApiModelProperty("模板")
	public String template_editor = "";

	@ApiModelProperty("校验码")
	public String md5 = "";

	public InvoicePrintInitInfo(Object content) {
		this.fromContent(content);
	}

	public String toInitString() {
		JSONObject jsonObject= JSON.parseObject(this.template_editor);
		return "InvoicePrintInitInfo.builder()\n\t\t\t\t" +
				".printmodule(\"" + this.printmodule + "\")\n\t\t\t\t" +
				".printname(\"" + this.printname + "\")\n\t\t\t\t" +
				".printkeyname(\"" + this.printkeyname + "\")\n\t\t\t\t" +
				".status(\"" + this.status + "\")\n\t\t\t\t" +
				".md5(\"" + this.md5 + "\")\n\t\t\t\t" +
				".template_editor(\"" + toJSONString(jsonObject,SerializerFeature.UseSingleQuotes) + "\")\n\t\t\t\t" +
				".build()";
	}

	/***
	 * JSON字符串使用单引号，解决字符串双引号错误
	 * @param object
	 * @param features
	 * @return
	 */
	public static String toJSONString(Object object,SerializerFeature...features){
		try (SerializeWriter out = new SerializeWriter()) {
			JSONSerializer serializer = new JSONSerializer(out);
			for (SerializerFeature feature : features) {
				serializer.config(feature, true);
			}
			serializer.write(object);
			return out.toString();
		}
	}
}
