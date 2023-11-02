package com.qnkj.core.plugins.netty.entitys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 邹智敏
 * @since 2018-11-24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value="Chatmsg对象", description="")
public class Chatmsg implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "序列号")
    private String id;

    @ApiModelProperty(value = "发送人id")
    private String senderId;

    @ApiModelProperty(value = "接收人id")
    private String receiverId;

    @ApiModelProperty(value = "接收的消息")
    private String context;

    @ApiModelProperty(value = "消息签收状态")
    private String status;

    @ApiModelProperty(value = "发送消息的时间")
    private Long timestamp;

    @ApiModelProperty(value = "发送消息的类型")
    private String type;

}
