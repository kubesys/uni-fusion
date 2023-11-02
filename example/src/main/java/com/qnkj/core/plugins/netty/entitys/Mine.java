package com.qnkj.core.plugins.netty.entitys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
@ToString
@ApiModel(value="Mine对象", description="")
public class Mine implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "应用名称")
    private String application = "";

    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "我的昵称")
    private String username;

    @ApiModelProperty(value = "我的头像")
    private String avatar;

    @ApiModelProperty(value = "活动次数")
    private Integer action = 1;

    @ApiModelProperty(value = "在线状态 online => 在线、offline =>离线 ")
    private String status;

    @ApiModelProperty(value = "消息")
    private String content = "";

    @ApiModelProperty(value = "发送者ID")
    private String fromid;

    @ApiModelProperty(value = "发送消息的时间")
    private Long timestamp;

    @ApiModelProperty(value = "扩展字段")
    private String extand;

    public Mine(){

    }

    public Mine(String id){
        this.id = id;
    }
}
