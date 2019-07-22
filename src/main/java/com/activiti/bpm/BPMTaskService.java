package com.activiti.bpm;

import java.util.List;
import java.util.Optional;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Service;

import com.activiti.common.PagingResult;
import com.activiti.model.AbstractTaskParameter;

@Service
public class BPMTaskService extends BPMBasicService {

    public Task getTask(String processInstanceId, String taskDefKey) {
        TaskQuery taskQuery = this.getTaskService().createTaskQuery();

        return taskQuery.processInstanceId(processInstanceId).taskDefinitionKey(taskDefKey).singleResult();
    }

    public void completeTask(AbstractTaskParameter taskParameter) {
        // set the user who excecuted the task
        identityService.setAuthenticatedUserId(taskParameter.getUserId());
        TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId(taskParameter.getProcessInsId()).or()
            .taskDefinitionKey(taskParameter.getTaskIdOrDefKey()).taskId(taskParameter.getTaskIdOrDefKey()).endOr()
            .active();

        Task task = taskQuery.singleResult();

        if (task != null) {
            taskService.setAssignee(task.getId(), taskParameter.getUserId());
            taskParameter.setTaskIdOrDefKey(task.getId());
            completeWithComment(task, taskParameter);
        }

    }

    protected void completeWithComment(Task task, AbstractTaskParameter taskParameter) {

        addComment(task.getId(), taskParameter.getProcessInsId(), taskParameter.getCommentAction(),
            taskParameter.getComment());

        taskService.complete(task.getId(), taskParameter.getVars());
    }

    protected void addComment(String taskId, String processInsId, String action, String comment) {
        if (comment != null) {
            // String fmtComment = action + " " + comment;
            taskService.addComment(taskId, processInsId, comment);
        }
    }

    public PagingResult getTaskPagingByAssignee(String processDefKey, String taskDefinitionKey, String assignee,
        Integer pageIndex, Integer pageSize) {
        PagingResult pagingResult = new PagingResult();

        long count = this.getTaskService().createTaskQuery().processDefinitionKey(processDefKey)
            .taskDefinitionKey(taskDefinitionKey).taskAssignee(assignee).count();
        pagingResult.setCount(count);

        if (pageIndex != null && pageSize != null) {
            if (count > 0) {
                List<Task> tasks = this.getTaskService().createTaskQuery().processDefinitionKey(processDefKey)
                    .taskDefinitionKey(taskDefinitionKey).taskAssignee(assignee).orderByTaskCreateTime().desc()
                    .listPage(pageIndex, pageSize);
                pagingResult.setDetails(tasks);
            }

        } else {
            List<Task> tasks = this.getTaskService().createTaskQuery().processDefinitionKey(processDefKey)
                .taskDefinitionKey(taskDefinitionKey).taskAssignee(assignee).list();
            pagingResult.setDetails(tasks);
        }

        return pagingResult;
    }

    public List<HistoricActivityInstance> getHistoricalInstanceIdsByTaskDefKey(String taskDefKey) {
        HistoricActivityInstanceQuery historicQuery = this.getHistoryService().createHistoricActivityInstanceQuery();

        return historicQuery.activityId(taskDefKey).list();
    }

    public List<HistoricActivityInstance> getHistoricalInstanceIdsByAssigneeAndTaskDefKey(String userId,
        String taskDefKey) {
        HistoricActivityInstanceQuery historicQuery = this.getHistoryService().createHistoricActivityInstanceQuery();

        return historicQuery.taskAssignee(userId).activityId(taskDefKey).list();
    }

    public List<HistoricActivityInstance> getHistoricalInstanceIdsByProcInsId(String procInsId) {
        HistoricActivityInstanceQuery historicQuery = this.getHistoryService().createHistoricActivityInstanceQuery();

        return historicQuery.processInstanceId(procInsId).activityType("userTask")
            .orderByHistoricActivityInstanceStartTime().asc().list();
    }

    /**
     * @param String processDefKey, required field
     * @param String userId, empId
     * @param Integer pageIndex,pageSize, page param
     */
    public PagingResult pagingTasks(String processDefKey, String userId, Integer pageIndex, Integer pageSize) {

        PagingResult pagingResult = new PagingResult();

        long count = initNativeTaskQueryCount(processDefKey, userId).count();

        pagingResult.setCount(count);

        if (pageIndex != null && pageSize != null) {
            if (count > 0) {
                int firstResult = Optional.of(pageIndex).filter(p -> p > 0).map(mapper -> mapper * pageSize).orElse(0);
                List<Task> tasks = initNativeTaskQueryData(processDefKey, userId).listPage(firstResult, pageSize);
                pagingResult.setDetails(tasks);
            }
        } else {
            List<Task> tasks = initNativeTaskQueryData(processDefKey, userId).list();
            pagingResult.setDetails(tasks);
        }

        return pagingResult;
    }

    private NativeTaskQuery initNativeTaskQueryCount(String processDefKey, String userId) {
        NativeTaskQuery taskQuery = this.getTaskService().createNativeTaskQuery();

        StringBuffer sql = new StringBuffer(
            "select count(res.id_) from act_ru_task res left join act_ru_identitylink link on link.task_id_ = res.id_ ");
        sql.append("inner join act_re_procdef def on res.proc_def_id_ = def.id_ where def.key_ = #{processDefKey} ");
        if (userId != null) {
            sql.append(
                "and (res.assignee_ = #{userId} or (res.assignee_ is null and link.type_ = 'candidate' and link.user_id_ = #{userId} ))");
        }

        taskQuery.sql(sql.toString());
        taskQuery.parameter("processDefKey", processDefKey);
        if (userId != null) {
            taskQuery.parameter("userId", userId);
        }

        return taskQuery;
    }

    private NativeTaskQuery initNativeTaskQueryData(String processDefKey, String userId) {
        NativeTaskQuery taskQuery = this.getTaskService().createNativeTaskQuery();

        StringBuffer sql = new StringBuffer("select res.id_, res.rev_, res.execution_id_, res.proc_inst_id_,"
            + "res.proc_def_id_, res.name_, res.parent_task_id_, res.description_, res.task_def_key_, res.owner_,"
            + "res.assignee_, res.delegation_, res.priority_, res.create_time_, res.due_date_,"
            + "res.category_,res.suspension_state_, res.tenant_id_, res.form_key_, res.claim_time_ from act_ru_task res "
            + "left join act_ru_identitylink link on link.task_id_ = res.id_ inner join act_re_procdef def "
            + "on res.proc_def_id_ = def.id_ where def.key_ = #{processDefKey} ");

        if (userId != null) {
            sql.append(
                "and (res.assignee_ = #{userId} or (res.assignee_ is null and link.type_ = 'candidate' and link.user_id_ = #{userId} ))");
        }

        sql.append(" order by res.create_time_ desc");

        taskQuery.sql(sql.toString());
        taskQuery.parameter("processDefKey", processDefKey);
        if (userId != null) {
            taskQuery.parameter("userId", userId);
        }

        return taskQuery;
    }

    public void reassignCandidateUser(AbstractTaskParameter taskParameter, String assignee) {
        Task task = taskService.createTaskQuery().processInstanceId(taskParameter.getProcessInsId())
            .taskId(taskParameter.getTaskIdOrDefKey()).taskCandidateUser(taskParameter.getUserId()).singleResult();

        if (task == null) {
            identityService.setAuthenticatedUserId(taskParameter.getUserId());

            taskService.addCandidateUser(taskParameter.getTaskIdOrDefKey(), taskParameter.getUserId());
            if (assignee != null) {
                taskService.deleteCandidateUser(taskParameter.getTaskIdOrDefKey(), assignee);
            }

            addComment(taskParameter.getTaskIdOrDefKey(), taskParameter.getProcessInsId(),
                taskParameter.getCommentAction(), taskParameter.getComment());
        }

    }

    public List<IdentityLink> getIdentityLinkByTaskId(String taskId) {
        return taskService.getIdentityLinksForTask(taskId);
    }
}
