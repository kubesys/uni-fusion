package com.qnkj.core.webconfigs;

import com.qnkj.common.configs.BasePicklistConfig;
import com.qnkj.common.entitys.PickList;
import com.qnkj.common.entitys.PickListEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * create by 徐雁
 * create date 2020/12/23
 */
@Slf4j
@Component("CorePicklistConfig")
public class PicklistConfig {

    @PostConstruct
    public void init() {
        /**
         * picklists：数据字典
         *
         * */
        BasePicklistConfig.addBuiltinPicklists(
                Arrays.asList(
                        new PickList().builtin(true).picklistname("yesno").picklistlabel("是否").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().intval(0).label("否").strval("no"),
                                        new PickListEntity().intval(1).label("是").strval("yes")
                                )
                        ),
                        new PickList().builtin(true).picklistname("status").picklistlabel("启用状态").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().intval(0).label("启用").strval("Active").styclass("layui-btn-start"),
                                        new PickListEntity().intval(1).label("停用").strval("Inactive").styclass("layui-btn-stop")
                                )
                        ),
                        new PickList().builtin(true).picklistname("include_date").picklistlabel("是否包含").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().intval(0).label("否").strval("no"),
                                        new PickListEntity().intval(1).label("是").strval("yes")
                                )
                        ),
                        new PickList().builtin(true).picklistname("platformtype").picklistlabel("平台类型").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("总部").strval("hq").intval(0),
                                        new PickListEntity().label("办事处").strval("office").intval(1),
                                        new PickListEntity().label("合作商").strval("partner").intval(2),
                                        new PickListEntity().label("承包商").strval("contractor").intval(3)
                                )
                        ),
                        new PickList().builtin(true).picklistname("istop").picklistlabel("是否置顶").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().intval(0).label("否").strval("no"),
                                        new PickListEntity().intval(1).label("是").strval("yes")
                                )
                        ),
                        new PickList().builtin(true).picklistname("isrelease").picklistlabel("是否发布").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().intval(0).label("否").strval("no"),
                                        new PickListEntity().intval(4).label("是").strval("yes")
                                )
                        ),
                        new PickList().builtin(true).picklistname("approvalstatus").picklistlabel("审批状态").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().intval(0).label("未审核").strval("JustCreated").styclass("table-picklist-grey"),
                                        new PickListEntity().intval(1).label("审核中").strval("Approvaling").styclass("table-picklist-bule"),
                                        new PickListEntity().intval(2).label("已审核").strval("Agree").styclass("table-picklist-green"),
                                        new PickListEntity().intval(3).label("未通过").strval("Disagree").styclass("table-picklist-red"),
                                        new PickListEntity().intval(4).label("已提交").strval("Valid").styclass("table-picklist-green"),
                                        new PickListEntity().intval(5).label("已暂停").strval("suspend").styclass("table-picklist-red")
                                )
                        ),
                        new PickList().builtin(true).picklistname("isdefault").picklistlabel("是否默认").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().intval(0).label("是").strval("yes"),
                                        new PickListEntity().intval(1).label("否").strval("no")
                                )
                        ),

                        new PickList().builtin(true).picklistname("is_admin").picklistlabel("用户类型").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().intval(0).label("普通用户").strval("pt"),
                                        new PickListEntity().intval(2).label("管理员").strval("admin")
                                )
                        ),
                        new PickList().builtin(true).picklistname("noticetype").picklistlabel("通知类型").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().intval(0).label("个人通知").strval("profile"),
                                        new PickListEntity().intval(1).label("客户通知").strval("supplier"),
                                        new PickListEntity().intval(2).label("审核通知").strval("approval"),
                                        new PickListEntity().intval(3).label("系统通知").strval("system")
                                )
                        ),
                        new PickList().builtin(true).picklistname("noticelevel").picklistlabel("通知级别").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().intval(0).label("通知").strval("info"),
                                        new PickListEntity().intval(1).label("警告").strval("warn"),
                                        new PickListEntity().intval(2).label("错误").strval("error"),
                                        new PickListEntity().intval(3).label("严重事件").strval("fatal")
                                )
                        ),
                        new PickList().builtin(true).picklistname("menutype").picklistlabel("菜单类型").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().intval(0).label("通用").strval("general"),
                                        new PickListEntity().intval(1).label("系统").strval("system"),
                                        new PickListEntity().intval(2).label("企业").strval("supplier")
                                )
                        ),
                        new PickList().builtin(true).picklistname("askstatus").picklistlabel("状态").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("未回复").strval("0").intval(0),
                                        new PickListEntity().label("已回复").strval("1").intval(1).sequence(1),
                                        new PickListEntity().label("多次回复").strval("2").intval(2).sequence(2),
                                        new PickListEntity().label("处理完毕").strval("3").intval(3).sequence(3),
                                        new PickListEntity().label("无法处理").strval("4").intval(4).sequence(4)
                                )
                        )
                )
        );
    }
}
