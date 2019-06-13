package com.kuyuner.workflow.manage.controller;

import com.kuyuner.common.controller.PageJson;
import com.kuyuner.workflow.manage.service.ReleasedService;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 流程管理-已发布流程
 *
 * @author tangzj
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/released")
public class ReleasedController {

    @Resource
    private ReleasedService releasedService;
    @Autowired
    private RepositoryService repositoryService;

    @RequestMapping("released")
    public String showEstablishList() {
        return "workflow/releasedList";
    }

    /**
     * 查询已发布流程
     */
    @ResponseBody
    @RequestMapping("/findReleasedWorkFlow")
    public PageJson findReleasedWorkFlow(String pageNum, String pageSize, String modelKey, String modelName) {
        return releasedService.findReleasedWorkFlow(pageNum, pageSize, modelKey, modelName);
    }

    /**
     * 显示流程图片
     *
     * @param processDefinitionId
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/imageResource", produces = {"image/png", "image/*"})
    public void imageResource(String processDefinitionId, HttpServletResponse response) throws IOException {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        String resourceName = processDefinition.getDiagramResourceName();
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                resourceName);
        response.addHeader("Content-Type", "image/png");
        response.addHeader("Content-Type", "image/*");
        byte[] b = new byte[1024];
        int len;
        OutputStream outputStream = response.getOutputStream();
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            outputStream.write(b, 0, len);
        }
        outputStream.flush();
        outputStream.close();
    }

}
