package com.activiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.activiti.bpm.BPMProcessService;
import com.activiti.common.OprResult;
import com.activiti.model.ActReDeployment;

@RestController
@RequestMapping(value = "/activiti")
public class ActivitiManageController {

    @Autowired
    private BPMProcessService bpmProcessService;

    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    public OprResult deployProcess(@RequestBody ActReDeployment actReDeployment, MultipartFile file) throws Exception {
        bpmProcessService.deployProcess(actReDeployment, file);
        return OprResult.success();
    }

}
