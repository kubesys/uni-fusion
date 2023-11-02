package com.qnkj.core.utils;

import com.qnkj.common.utils.CallbackUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.entitys.WorkflowStatus;
import com.qnkj.core.base.services.IBaseService;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * create by 徐雁
 * create date 2021/9/25
 * create time 9:01 上午
 */

@Slf4j
public class WorkflowUtils {
    private WorkflowUtils() { }

    private static Boolean canDefinitionFlow(String modulename) {
        return !Arrays.asList("workflowmanage", "Approvalflowmanage","Announcements","Notices","ask").contains(modulename);
    }

    @ApiModelProperty("同步流程中心数据")
    public static void synchronizationWorkFlows(String modulename,Object entity) {
        try {
            CallbackUtils.invokeByModule("flowlaunch", "synchronizationWorkFlows", IBaseService.class, modulename, entity);
        } catch (Exception e) {
            log.warn("==========================================================");
            log.warn("同步流程中心数据 ： Module：SynchronizationWorkFlows");
            log.warn("同步流程中心数据 ： Error：" + e.getMessage());
            log.warn("==========================================================");
        }
    }

    @ApiModelProperty("同步审批中心数据")
    public static void synchronizationApprovalFlows(String modulename,Object entity) {
        try {
            CallbackUtils.invokeByModule("approvalcenter", "synchronizationApprovalFlows", IBaseService.class, modulename, entity);
        } catch (Exception e) {
            log.warn("==========================================================");
            log.warn("同步流程中心数据 ： Module：SynchronizationApprovalFlows");
            log.warn("同步流程中心数据 ： Error：" + e.getMessage());
            log.warn("==========================================================");
        }
    }

    @ApiModelProperty("启动业务流程")
    public static Boolean startWorkflow(String modulename, String record) {
        if(Boolean.FALSE.equals(canDefinitionFlow(modulename))) {
            return false;
        }
        try {
            Object invoke = CallbackUtils.invokeByModule("workflowmanage", "StartWorkflow", IBaseService.class, modulename,record);
            if(Utils.isNotEmpty(invoke)) {
                return (Boolean) invoke;
            }
        } catch (Exception e) {
            log.warn("==========================================================");
            log.warn("启动业务流程 ： Module：StartWorkflow");
            log.warn("启动业务流程 ： modulename：" + modulename);
            log.warn("启动业务流程 ： Error：" + e.getMessage());
            log.warn("==========================================================");
        }
        return false;
    }

    @ApiModelProperty("启动审批流程")
    public static Boolean startApprovalflow(String modulename, String record) {
        if(Boolean.FALSE.equals(canDefinitionFlow(modulename))) {
            return false;
        }
        try {
            Object invoke = CallbackUtils.invokeByModule("workflowmanage", "StartApprovalflow", IBaseService.class, modulename,record);
            if(Utils.isNotEmpty(invoke)) {
                return (Boolean) invoke;
            }
        } catch (Exception e) {
            log.warn("==========================================================");
            log.warn("启动业务流程 ： Module：StartApprovalflow");
            log.warn("启动业务流程 ： modulename：" + modulename);
            log.warn("启动业务流程 ： Error：" + e.getMessage());
            log.warn("==========================================================");
        }
        return false;
    }

    @ApiModelProperty("指定模块是否配置流程")
    public static WorkflowStatus getWorkflowStatus(String modulename) {
        return getWorkflowStatus(modulename, "");
    }
    public static WorkflowStatus getWorkflowStatus(String modulename, String record) {
        if(Boolean.FALSE.equals(canDefinitionFlow(modulename))) {
            return new WorkflowStatus();
        }
        try {
            Object invoke = CallbackUtils.invokeByModule("workflowmanage", "getWorkflowStatus", IBaseService.class, modulename,record);
            if(Utils.isNotEmpty(invoke)) {
                return (WorkflowStatus)invoke;
            }
        } catch (Exception e) {
            log.warn("==========================================================");
            log.warn("获取流程各种状态 ： Module：getWorkflowStatus");
            log.warn("获取流程各种状态 ： modulename：" + modulename);
            log.warn("获取流程各种状态 ： Error：" + e.getMessage());
            log.warn("==========================================================");
        }
        return new WorkflowStatus();
    }

    @ApiModelProperty("获取指定模块流程的活动环节")
    public static Object getWorkflowActive(String modulename, String record) {
        if(Boolean.FALSE.equals(canDefinitionFlow(modulename))) {
            return null;
        }
        try {
            return CallbackUtils.invokeByModule("workflowmanage", "getWorkflowActive", IBaseService.class, modulename, record);
        } catch (Exception e) {
            log.warn("==========================================================");
            log.warn("获取流程当前活动环节 ： Module：getWorkflowActive");
            log.warn("获取流程当前活动环节 ： modulename：" + modulename);
            log.warn("获取流程当前活动环节 ： record：" + record);
            log.warn("获取流程当前活动环节 ： Error：" + e.getMessage());
            log.warn("==========================================================");
        }
        return null;
    }

    @ApiModelProperty("指定模块流程BPMN数据")
    public static Object getWorkflowBpmn(String modulename, String record) {
        if(Boolean.FALSE.equals(canDefinitionFlow(modulename))) {
            return null;
        }
        try {
            return CallbackUtils.invokeByModule("workflowmanage", "getWorkflowBpmn", IBaseService.class, modulename, record);
        } catch (Exception e) {
            log.warn("==========================================================");
            log.warn("获取BPMN数据 ： Module：getWorkflowBpmn");
            log.warn("获取BPMN数据 ： modulename：" + modulename);
            log.warn("获取BPMN数据 ： record：" + record);
            log.warn("获取BPMN数据 ： Error：" + e.getMessage());
            log.warn("==========================================================");
        }
        return null;
    }

    @ApiModelProperty("指定模块流程的当前环节表单设置")
    public static Object getWorkflowFormSettings(String modulename, String record) {
        if(Boolean.FALSE.equals(canDefinitionFlow(modulename))) {
            return null;
        }
        try {
            return CallbackUtils.invokeByModule("workflowmanage", "getWorkflowFormSettings", IBaseService.class, modulename, record);
        } catch (Exception e) {
            log.warn("==========================================================");
            log.warn("获取流程的当前环节表单设置 ： Module：getWorkflowFormSettings");
            log.warn("获取流程的当前环节表单设置 ： modulename：" + modulename);
            log.warn("获取流程的当前环节表单设置 ： record：" + record);
            log.warn("获取流程的当前环节表单设置 ： Error：" + e.getMessage());
            log.warn("==========================================================");
        }
        return null;
    }

    @ApiModelProperty("获取流程当前处理配置")
    public static Object getWorkflowTransact(String modulename, String record) {
        if(Boolean.FALSE.equals(canDefinitionFlow(modulename))) {
            return null;
        }
        try {
            return CallbackUtils.invokeByModule("workflowmanage", "getWorkflowTransact", IBaseService.class, modulename, record);
        } catch (Exception e) {
            log.warn("==========================================================");
            log.warn("获取流程当前处理配置 ： Module：getWorkflowTransact");
            log.warn("获取流程当前处理配置 ： modulename：" + modulename);
            log.warn("获取流程当前处理配置 ： record：" + record);
            log.warn("获取流程当前处理配置 ： Error：" + e.getMessage());
            log.warn("==========================================================");
        }
        return null;
    }

    @ApiModelProperty("完成指定模块当前环节")
    public static Object completeWorkflow(String modulename, String record, String reply, Object nextDealwithInfo) {
        if(Boolean.FALSE.equals(canDefinitionFlow(modulename))) {
            return null;
        }
        try {
            return CallbackUtils.invokeByModule("workflowmanage", "completeWorkflow", IBaseService.class, modulename, record, reply, nextDealwithInfo);
        } catch (Exception e) {
            log.warn("==========================================================");
            log.warn("完成流程当前环节 ： Module：completeWorkflow");
            log.warn("完成流程当前环节 ： modulename：" + modulename);
            log.warn("完成流程当前环节 ： record：" + record);
            log.warn("完成流程当前环节 ： nextDealwithInfo：" + nextDealwithInfo);
            log.warn("完成流程当前环节 ： Error：" + e.getMessage());
            log.warn("==========================================================");
        }
        return false;
    }

    @ApiModelProperty("撤回指定模块的业务流程")
    public static Boolean evacuateWorkflowInstance(String modulename, String record, String reply) {
        if(Boolean.FALSE.equals(canDefinitionFlow(modulename))) {
            return false;
        }
        try {
            return (Boolean) CallbackUtils.invokeByModule("workflowmanage", "evacuateWorkflowInstance", IBaseService.class, modulename, record, reply);
        } catch (Exception e) {
            log.warn("==========================================================");
            log.warn("撤回业务流程 ： Module：evacuateWorkflowInstance");
            log.warn("撤回业务流程 ： modulename：" + modulename);
            log.warn("撤回业务流程 ： record：" + record);
            log.warn("撤回业务流程 ： reply：" + reply);
            log.warn("撤回业务流程 ： Error：" + e.getMessage());
            log.warn("==========================================================");
        }
        return false;
    }

    @ApiModelProperty("获取流程当前退回信息")
    public static Object getWorkflowRollback(String modulename, String record) {
        if(Boolean.FALSE.equals(canDefinitionFlow(modulename))) {
            return null;
        }
        try {
            return CallbackUtils.invokeByModule("workflowmanage", "getWorkflowRollback", IBaseService.class, modulename, record);
        } catch (Exception e) {
            log.warn("==========================================================");
            log.warn("获取流程当前退回信息 ： Module：getWorkflowRollback");
            log.warn("获取流程当前退回信息 ： modulename：" + modulename);
            log.warn("获取流程当前退回信息 ： record：" + record);
            log.warn("获取流程当前退回信息 ： Error：" + e.getMessage());
            log.warn("==========================================================");
        }
        return null;
    }

    @ApiModelProperty("删除指定的业务流程")
    public static void deletedWorkflowInstance(String modulename, String record) {
        if(Boolean.FALSE.equals(canDefinitionFlow(modulename))) {
            return;
        }
        try {
            CallbackUtils.invokeByModule("workflowmanage", "deletedWorkflowInstance", IBaseService.class, modulename, record);
        } catch (Exception e) {
            log.warn("==========================================================");
            log.warn("删除指定的业务流程 ： Module：deletedWorkflowInstance");
            log.warn("删除指定的业务流程 ： modulename：" + modulename);
            log.warn("删除指定的业务流程 ： record：" + record);
            log.warn("删除指定的业务流程 ： Error：" + e.getMessage());
            log.warn("==========================================================");
        }
    }
}
