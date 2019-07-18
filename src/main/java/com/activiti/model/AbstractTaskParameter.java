package com.activiti.model;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTaskParameter {

    private String processInsId;

    private String taskIdOrDefKey;

    private String userId;

    private String commentAction;

    private String comment;

    protected Map<String, Object> vars = new HashMap<String, Object>();

    public AbstractTaskParameter(String processInsId, String taskIdOrDefKey, String userId) {
        this.setProcessInsId(processInsId);
        this.setTaskIdOrDefKey(taskIdOrDefKey);
        this.setUserId(userId);
    }

    public String getProcessInsId() {
        return processInsId;
    }

    public void setProcessInsId(String processInsId) {
        this.processInsId = processInsId;
    }

    public String getTaskIdOrDefKey() {
        return taskIdOrDefKey;
    }

    public void setTaskIdOrDefKey(String taskIdOrDefKey) {
        this.taskIdOrDefKey = taskIdOrDefKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFlowCondition() {
        return (String)vars.get("flowCondition");
    }

    public void setFlowCondition(String flowCondition) {
        vars.put("flowCondition", flowCondition);
    }

    public String getNextTaskAssignee() {
        return (String)vars.get("taskAssignee");
    }

    public void setNextTaskAssignee(String nextTaskAssignee) {
        vars.put("taskAssignee", nextTaskAssignee);
    }

    public String getNextTaskCandidates() {
        return (String)vars.get("taskCandidates");
    }

    public void setNextTaskCandidates(String nextTaskCandidates) {
        vars.put("taskCandidates", nextTaskCandidates);
    }

    public String getNextTaskCandidateGroups() {
        return (String)vars.get("taskCandidateGroups");
    }

    public void setNextTaskCandidateGroups(String nextTaskCandidateGroups) {
        vars.put("taskCandidateGroups", nextTaskCandidateGroups);
    }

    public String getCommentAction() {
        return commentAction;
    }

    public void setCommentAction(String commentAction) {
        this.commentAction = commentAction;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setVars(Map<String, Object> vars) {
        this.vars = vars;
    }

    public Map<String, Object> getVars() {
        return vars;
    }

}
