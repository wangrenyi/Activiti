package com.activiti.bpm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.activiti.exception.SystemException;
import com.activiti.model.AbstractProcessParameter;
import com.activiti.model.ActReDeployment;

@Service
@Transactional(readOnly = true)
public class BPMProcessService extends BPMBasicService {
    private static Logger LOG = LoggerFactory.getLogger(BPMProcessService.class);

    @Transactional
    public void deployProcess(ActReDeployment actReDeployment, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(fileName);

        try {
            InputStream fileInputStream = file.getInputStream();

            if (extension.equals("zip") || extension.equals("bar")) {
                this.deployZip(actReDeployment, fileInputStream);
            } else if (extension.equals("bpmn")) {
                String baseName = FilenameUtils.getBaseName(fileName);
                this.deployFile(actReDeployment, baseName + ".bpmn20.xml", fileInputStream);
            } else if (extension.equals("xml")) {
                this.deployFile(actReDeployment, fileName, fileInputStream);
            }
        } catch (IOException e) {
            throw new SystemException("File loading exception.", e);
        }

    }

    public String startProcess(AbstractProcessParameter processParameter) {
        identityService.setAuthenticatedUserId(processParameter.getUserId());

        ProcessInstance proInstance = runtimeService.startProcessInstanceByKey(processParameter.getProcessDefKey(),
            processParameter.getBizKey(), processParameter.getVars());

        return proInstance.getId();

    }

    public void suspendProcessInstanceById(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    public void deleteProcessInstance(String processInstanceId, String deletedBy, String deleteReason) {
        ProcessInstance processInstance
            = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (processInstance != null) {
            runtimeService.deleteProcessInstance(processInstanceId, deletedBy + deleteReason);
        }
    }

    @Transactional
    public void deleteHistoricProcessInstance(String processInstanceId) {
        historyService.deleteHistoricProcessInstance(processInstanceId);
    }

    private Deployment deployZip(ActReDeployment actReDeployment, InputStream fileInputStream) {
        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);

        DeploymentBuilder deployment = repositoryService.createDeployment();
        return deployment.name(actReDeployment.getName()).category(actReDeployment.getCategory())
            .key(actReDeployment.getKey()).addZipInputStream(zipInputStream).deploy();
    }

    private Deployment deployClassPathFile(ActReDeployment actReDeployment, String resourceName) {
        DeploymentBuilder deployment = repositoryService.createDeployment();
        return deployment.addClasspathResource(resourceName).name(actReDeployment.getName()).deploy();
    }

    private Deployment deployFile(ActReDeployment actReDeployment, String resourceName, InputStream fileInputStream)
        throws FileNotFoundException {
        DeploymentBuilder deployment = repositoryService.createDeployment();

        return deployment.name(actReDeployment.getName()).category(actReDeployment.getCategory())
            .key(actReDeployment.getKey()).addInputStream(resourceName, fileInputStream).deploy();
    }

    private Deployment deployFile(ActReDeployment actReDeployment, String resourceName, String filePath)
        throws FileNotFoundException {
        DeploymentBuilder deployment = repositoryService.createDeployment();

        return deployment.name(actReDeployment.getName()).category(actReDeployment.getCategory())
            .key(actReDeployment.getKey()).addInputStream(resourceName, new FileInputStream(filePath)).deploy();
    }
}
