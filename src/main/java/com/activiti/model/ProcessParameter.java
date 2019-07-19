 package com.activiti.model;

public class ProcessParameter extends AbstractProcessParameter {
    public ProcessParameter() {

    }
    public ProcessParameter(String processDefKey, String bizKey, String userId) {
        this.setProcessDefKey(processDefKey);
        this.setBizKey(bizKey);
        this.setUserId(userId);
    }
}

