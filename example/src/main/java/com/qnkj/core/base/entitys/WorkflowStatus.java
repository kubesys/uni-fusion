package com.qnkj.core.base.entitys;

import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2021/10/9
 * create time 11:03 下午
 */

public class WorkflowStatus {
    protected Boolean IsDefineflow = false;  //是否定义了流程
    protected Boolean IsStartflow = false;   //是否启动流程
    protected Boolean IsFinished = false;   //流程是否结束
    protected Boolean IsSuspend = false;    //流程是否暂停
    protected Boolean IsDealwith = false;   //当前用户是否是处理人
    protected Boolean IsFirstNode = false;  //当前处理环节是否为第一个环节
    protected Boolean IsStartUser = false;  //当前用户是否能发起流程
    protected Boolean IsWaitupper = false;  //当前环节是否等待前环节办理完
    protected Boolean IsRollBack = false;   //当前环节是否可退回
    protected Boolean IsApproval = false;   //是否为审批流程
    protected List<Map<String,Object>> waitUppers = null;

    public WorkflowStatus() {

    }

    public Boolean isDealwith() {
        return IsDealwith;
    }

    public Boolean isFinished() {
        return IsFinished;
    }

    public Boolean isSuspend() {
        return IsSuspend;
    }

    public Boolean isDefineflow() {
        return IsDefineflow;
    }

    public Boolean isStartflow() {
        return IsStartflow;
    }

    public Boolean isFirstNode() {
        return IsFirstNode;
    }

    public Boolean isStartUser() {
        return IsStartUser;
    }

    public Boolean isWaitupper() {
        return IsWaitupper;
    }

    public List<Map<String,Object>> getWaitUppers() {
        return waitUppers;
    }

    public Boolean isRollBack() {
        return IsRollBack;
    }

    public Boolean isApproval() {
        return IsApproval;
    }

    public Boolean isWorkflow() {
        return !IsApproval;
    }

    public WorkflowStatus setDealwith(Boolean dealwith) {
        IsDealwith = dealwith;
        return this;
    }

    public WorkflowStatus setFinished(Boolean finished) {
        IsFinished = finished;
        return this;
    }

    public WorkflowStatus setSuspend(Boolean suspend) {
        IsSuspend = suspend;
        return this;
    }

    public WorkflowStatus setDefineflow(Boolean workflow) {
        this.IsDefineflow = workflow;
        return this;
    }

    public WorkflowStatus setStartflow(Boolean startflow) {
        this.IsStartflow = startflow;
        return this;
    }

    public WorkflowStatus setFirstNode(Boolean firstNode) {
        this.IsFirstNode = firstNode;
        return this;
    }

    public WorkflowStatus setStartUser(Boolean startUser) {
        this.IsStartUser = startUser;
        return this;
    }

    public WorkflowStatus setWaitupper(Boolean waitupper) {
        this.IsWaitupper = waitupper;
        return this;
    }

    public WorkflowStatus setWaitUppers(List<Map<String, Object>> waitUppers) {
        this.waitUppers = waitUppers;
        return this;
    }

    public WorkflowStatus setRollBack(Boolean rollBack) {
        this.IsRollBack = rollBack;
        return this;
    }

    public WorkflowStatus setApproval(Boolean approval) {
        this.IsApproval = approval;
        return this;
    }
}
