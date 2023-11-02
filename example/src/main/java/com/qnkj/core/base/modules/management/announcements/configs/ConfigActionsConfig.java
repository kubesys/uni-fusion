package com.qnkj.core.base.modules.management.announcements.configs;

import com.qnkj.common.configs.BaseActionsConfig;
import com.qnkj.common.entitys.Action;
import com.qnkj.core.base.modules.management.announcements.services.IAnnouncementsService;

import java.util.Arrays;

/**
* @author Auto Generator
* @date 2020-11-17
*/
public class ConfigActionsConfig extends BaseActionsConfig {
	public ConfigActionsConfig() {
		/*Actions 配置*/
		actions = Arrays.asList(
			new Action().actionkey("SubmitOnline").actionlabel("发布公告").actiontype("editview").toggle("ajax").confirm("确定要发布吗？发布后将无法再编辑！").funclass(IAnnouncementsService.class.getName()).funpara("SubmitOnline").actionclass("layui-btn-warm"),
			new Action().actionkey("SetTop").actionlabel("置顶").toggle("dialog").group("ids").digwidth("400px"),
			new Action().actionkey("CancelRelease").actionlabel("撤消发布").toggle("ajax").confirm("确定要撤消这些公告的发布吗？").group("ids").actionclass("layui-btn-grey")
		);

	}
}
