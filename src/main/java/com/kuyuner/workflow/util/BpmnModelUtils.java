package com.kuyuner.workflow.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kuyuner.workflow.engine.image.CustomProcessDiagramGenerator;
import com.kuyuner.workflow.engine.image.HighLightFlowElement;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowElementsContainer;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Gateway;
import org.activiti.bpmn.model.InclusiveGateway;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tangzj
 */
public class BpmnModelUtils {

    /**
     * 将上传的Bpmn文件转成BpmnModel对象
     *
     * @param request
     * @return
     * @throws FileUploadException
     * @throws IOException
     */
    public static String uploadFileToBpmnModel(HttpServletRequest request)
            throws FileUploadException, IOException {

        InputStream inputStream = BpmnModelUtils.fileInputStream(request);
        if (inputStream != null) {
            try {
                StringBuilder bpmnJson = new StringBuilder();
                byte[] bytes = new byte[1024];
                int l;
                while ((l = inputStream.read(bytes)) > 0) {
                    bpmnJson.append(new String(bytes, 0, l, "UTF-8"));
                }
                return bpmnJson.toString();
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }

        return null;
    }

    /**
     * ServletFileUpload
     *
     * @param request
     * @return
     * @throws IOException
     * @throws FileUploadException
     */
    private static InputStream fileInputStream(HttpServletRequest request) throws IOException {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (!multipartResolver.isMultipart(request)) {
            return null;
        }

        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        Iterator iter = multiRequest.getFileNames();
        while (iter.hasNext()) {
            MultipartFile multipartFile = multiRequest.getFile(iter.next().toString());
            if (multipartFile != null) {
                return multipartFile.getInputStream();
            }
        }

        return null;

    }

    /**
     * 模型转成XML字符串
     *
     * @param bpmnModel
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String bpmnToXml(BpmnModel bpmnModel) throws UnsupportedEncodingException {
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        String bpmn20Xml = new String(bpmnXMLConverter.convertToXML(bpmnModel), "UTF-8");
        return bpmn20Xml;
    }

    /**
     * 模型转JSON对象
     *
     * @param bpmnModel
     * @return
     */
    public static ObjectNode bpmnToObjectNode(BpmnModel bpmnModel) {
        return new BpmnJsonConverter().convertToJson(bpmnModel);
    }

    /**
     * 模型转JSON字符串
     *
     * @param bpmnModel
     * @return
     * @throws JsonProcessingException
     */
    public static String bpmnToJson(BpmnModel bpmnModel) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(bpmnToObjectNode(bpmnModel));
    }

    /**
     * JSON字符串转模型
     *
     * @param json
     * @return
     * @throws IOException
     */
    public static BpmnModel jsonToBpmnModel(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return jsonToBpmnModel(objectMapper.readTree(json));
    }

    /**
     * JSON对象转模型
     *
     * @param jsonNode
     * @return
     */
    public static BpmnModel jsonToBpmnModel(JsonNode jsonNode) {
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);
        return bpmnModel;
    }

    /**
     * XML字符串转模型
     *
     * @param bpmn20Xml
     * @return
     * @throws UnsupportedEncodingException
     */
    public static BpmnModel xmlToBpmnModel(String bpmn20Xml) throws UnsupportedEncodingException {
        BpmnXMLConverter converter = new BpmnXMLConverter();
        BpmnModel bpmnModel = converter.convertToBpmnModel(
                new InputStreamSource(new ByteArrayInputStream(bpmn20Xml.getBytes("UTF-8"))), true, false, "UTF-8");
        return bpmnModel;
    }

    /**
     * 模型生成PNG图片
     *
     * @param bpmnModel
     * @param processEngineConfiguration
     * @return
     * @throws IOException
     */
    public static byte[] bpmnToPngWithHighLight(BpmnModel bpmnModel,
                                                ProcessEngineConfiguration processEngineConfiguration, List<String> highLightedActivities,
                                                List<String> highLightedFlows) throws IOException {
        InputStream inputStream = null;
        highLightedActivities = highLightedActivities == null ? Collections.<String>emptyList() : highLightedActivities;
        highLightedFlows = highLightedFlows == null ? Collections.<String>emptyList() : highLightedFlows;
        try {
            inputStream = processEngineConfiguration.getProcessDiagramGenerator().generateDiagram(bpmnModel, "png",
                    highLightedActivities, highLightedFlows, processEngineConfiguration.getActivityFontName(),
                    processEngineConfiguration.getLabelFontName(), processEngineConfiguration.getClassLoader(), 1.0);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int l = 0;
            while ((l = inputStream.read(bytes)) > 0) {
                output.write(bytes, 0, l);
            }
            return output.toByteArray();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 模型生成PNG图片
     *
     * @param bpmnModel
     * @param processEngineConfiguration
     * @return
     * @throws IOException
     */
    public static byte[] bpmnToPng(BpmnModel bpmnModel, ProcessEngineConfiguration processEngineConfiguration)
            throws IOException {
        return bpmnToPngWithHighLight(bpmnModel, processEngineConfiguration, null, null);
    }

    /**
     * 模型生成PNG图片
     *
     * @param bpmnModel
     * @param processEngineConfiguration
     * @return
     * @throws IOException
     */
    public static byte[] bpmnToPngWithHighLight(BpmnModel bpmnModel, List<HighLightFlowElement> highLightedActivities,
                                                List<HighLightFlowElement> highLightedFlows, ProcessEngineConfiguration processEngineConfiguration)
            throws IOException {
        InputStream inputStream = null;
        highLightedActivities = highLightedActivities == null ? Collections.<HighLightFlowElement>emptyList()
                : highLightedActivities;
        highLightedFlows = highLightedFlows == null ? Collections.<HighLightFlowElement>emptyList() : highLightedFlows;
        try {
            CustomProcessDiagramGenerator customProcessDiagramGenerator = (CustomProcessDiagramGenerator) processEngineConfiguration
                    .getProcessDiagramGenerator();
            inputStream = customProcessDiagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivities,
                    highLightedFlows, processEngineConfiguration.getActivityFontName(),
                    processEngineConfiguration.getLabelFontName(), processEngineConfiguration.getClassLoader());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int l = 0;
            while ((l = inputStream.read(bytes)) > 0) {
                output.write(bytes, 0, l);
            }
            return output.toByteArray();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 查询所有的节点
     *
     * @param bpmnModel
     * @return
     */
    public static List<FlowNode> findAllFlowNodes(BpmnModel bpmnModel) {
        return findAllFlowElement(bpmnModel, FlowNode.class);
    }

    /**
     * 查询指定类型的元素
     *
     * @param bpmnModel
     * @param type
     * @return
     */
    public static <FlowElementType extends FlowElement> List<FlowElementType> findAllFlowElement(BpmnModel bpmnModel,
                                                                                                 Class<FlowElementType> type) {
        List<FlowElementType> flowNodes = new ArrayList<FlowElementType>();
        for (Process process : bpmnModel.getProcesses()) {
            flowNodes.addAll(findAllFlowNodes_(process, type));
        }
        return flowNodes;
    }

    /**
     * 迭代遍历所有节点
     *
     * @param flowElementsContainer
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <FlowElementType extends FlowElement> List<FlowElementType> findAllFlowNodes_(
            FlowElementsContainer flowElementsContainer, Class<FlowElementType> type) {
        List<FlowElementType> flowElements = new ArrayList<FlowElementType>();
        for (FlowElement flowElement : flowElementsContainer.getFlowElements()) {
            if (flowElement instanceof FlowElementsContainer) {
                flowElements.addAll(findAllFlowNodes_((FlowElementsContainer) flowElement, type));
            } else if (type.isInstance(flowElement)) {
                flowElements.add((FlowElementType) flowElement);
            }
        }
        return flowElements;
    }

    /**
     * 查询顺序流分支</br>
     * 这里默认任务只有一个出顺序流，从这个顺序流往下查询排它和包含网关，并返回网关的所有出顺序流</br>
     * 如果下一个节点就是任务，那么就返回出顺序流
     *
     * @param bpmnModel
     * @param taskKey
     * @return
     */
    public static List<SequenceFlow> findBranchSequenceFlow(BpmnModel bpmnModel, String taskKey) {
        UserTask userTask = (UserTask) getFlowElement(bpmnModel, taskKey);
        List<SequenceFlow> sequenceFlows = userTask.getOutgoingFlows();// 及时没有出顺序流，也不会为null
        if (sequenceFlows.size() == 1) {
            FlowNode node = (FlowNode) getFlowElement(bpmnModel, sequenceFlows.get(0).getTargetRef());
            if (node instanceof ExclusiveGateway || node instanceof InclusiveGateway) {
                return node.getOutgoingFlows();
            }
            return sequenceFlows;
        }

        throw new RuntimeException("UserTask[" + taskKey + "]没有出顺序流或者出顺序流大于1");
    }

    /**
     * 根据元素ID找到指定流元素，如果找不到，则返回null
     *
     * @param bpmnModel
     * @param sid
     * @return
     */
    public static FlowElement getFlowElement(BpmnModel bpmnModel, String sid) {
        sid = StringUtils.trimToEmpty(sid);
        List<FlowElement> flowElements = findAllFlowElement(bpmnModel, FlowElement.class);
        for (FlowElement flowElement : flowElements) {
            if (sid.equals(flowElement.getId())) {
                return flowElement;
            }
        }
        return null;
    }

    /**
     * 检查该任务是不是会签任务
     *
     * @param bpmnModel
     * @param taskKey
     * @return
     */
    public static boolean isCounterSign(BpmnModel bpmnModel, String taskKey) {
        UserTask userTask = (UserTask) getFlowElement(bpmnModel, taskKey);
        MultiInstanceLoopCharacteristics characteristics = userTask.getLoopCharacteristics();
        return characteristics != null;
    }

    /**
     * 查找第一个UserTask，要求UserTask是StartEvent的直连节点或者在直连StartEvent的排他分支下
     *
     * @param bpmnModel
     * @param startSequenceFlowName 如果UserTask是StartEvent的直连节，该值可为空
     * @return 找不到的话就为null
     */
    public static UserTask getFirstUserTask(BpmnModel bpmnModel, String startSequenceFlowName) {
        String flowNodeId = bpmnModel.getMainProcess().findFlowElementsOfType(StartEvent.class).get(0)
                .getOutgoingFlows().get(0).getTargetRef();
        FlowElement flowElement = getFlowElement(bpmnModel, flowNodeId);
        if (flowElement instanceof UserTask) {
            return (UserTask) flowElement;
        }

        if (flowElement instanceof ExclusiveGateway) {
            ExclusiveGateway gateway = (ExclusiveGateway) flowElement;
            if (StringUtils.isBlank(startSequenceFlowName)) {
                SequenceFlow sequenceFlow = (SequenceFlow) getFlowElement(bpmnModel, gateway.getDefaultFlow());
                return (UserTask) getFlowElement(bpmnModel, sequenceFlow.getTargetRef());
            }
            List<SequenceFlow> list = ((ExclusiveGateway) flowElement).getOutgoingFlows();
            for (SequenceFlow sequenceFlow : list) {
                if (sequenceFlow.getName().equals(startSequenceFlowName)) {
                    String sid = sequenceFlow.getTargetRef();
                    UserTask userTask = (UserTask) getFlowElement(bpmnModel, sid);
                    return userTask;
                }
            }
        }
        return null;
    }

    /**
     * 1、根据当前任务节点获得下一个任务节点，如果找不到则返回null <br></br>
     * 2、适用于当前的任务的下一个节点是任务或排它网关，如果是排它网关，就通过顺序流的名称判断是哪一个任务 <br></br>
     * 3、如果排他网关下面紧接着是并行网关，那么会返回多个UserTask
     *
     * @param bpmnModel
     * @param taskKey
     * @param sequenceFlowName
     * @return
     */
    public static List<UserTask> getNextUserTask(BpmnModel bpmnModel, String taskKey, String sequenceFlowName) {
        sequenceFlowName = StringUtils.stripToEmpty(sequenceFlowName);

        UserTask userTask = (UserTask) BpmnModelUtils.getFlowElement(bpmnModel, taskKey);

        List<SequenceFlow> sequenceFlows = userTask.getOutgoingFlows();
        if (sequenceFlows.size() > 0) {
            FlowElement element = BpmnModelUtils.getFlowElement(bpmnModel, sequenceFlows.get(0).getTargetRef());
            List<UserTask> userTasks = new ArrayList<>();
            if (element instanceof ExclusiveGateway) {
                Gateway gateway = (Gateway) element;
                List<SequenceFlow> list = gateway.getOutgoingFlows();
                for (SequenceFlow sequenceFlow : list) {
                    if (sequenceFlowName.equals(sequenceFlow.getName())) {
                        FlowElement flowElement = BpmnModelUtils.getFlowElement(bpmnModel, sequenceFlow.getTargetRef());
                        if (flowElement instanceof ParallelGateway) {
                            List<SequenceFlow> outgoingSequenceFlows = ((ParallelGateway) flowElement).getOutgoingFlows();
                            for (SequenceFlow flow : outgoingSequenceFlows) {
                                FlowElement ele = BpmnModelUtils.getFlowElement(bpmnModel, flow.getTargetRef());
                                if (ele instanceof UserTask) {
                                    userTasks.add((UserTask) ele);
                                }
                            }
                        } else if (flowElement instanceof UserTask) {
                            userTasks.add((UserTask) flowElement);
                        }
                        return userTasks.size() == 0 ? null : userTasks;
                    }
                }
            } else if (element instanceof UserTask) {
                userTasks.add((UserTask) element);
                return userTasks;
            } else if (element instanceof ParallelGateway) {
                SequenceFlow flow = ((ParallelGateway) element).getOutgoingFlows().get(0);
                FlowElement flowElement = BpmnModelUtils.getFlowElement(bpmnModel, flow.getTargetRef());
                if (flowElement instanceof UserTask) {
                    userTasks.add((UserTask) flowElement);
                }
                return userTasks.size() == 0 ? null : userTasks;
            }
        }

        return null;
    }

    /**
     * 检查顺序流名称是否正确
     *
     * @param bpmnModel
     * @param taskKey
     * @param sequenceFlowName
     */
    public static boolean checkSequenceFlowName(BpmnModel bpmnModel, String taskKey, String sequenceFlowName) {
        FlowNode flowNode = (FlowNode) BpmnModelUtils.getFlowElement(bpmnModel, taskKey);
        FlowElement flowElement = BpmnModelUtils.getFlowElement(bpmnModel, flowNode.getOutgoingFlows().get(0).getTargetRef());
        if (flowElement instanceof ExclusiveGateway) {
            List<SequenceFlow> flows = ((ExclusiveGateway) flowElement).getOutgoingFlows();
            sequenceFlowName = sequenceFlowName == null ? "" : sequenceFlowName;
            for (SequenceFlow sequenceFlow : flows) {
                if (sequenceFlowName.equals(sequenceFlow.getName())) {
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    /**
     * 获得下一环节可选的顺序流名称
     *
     * @param bpmnModel
     * @param taskKey
     */
    public static List<String> findNextSequenceFlowNames(BpmnModel bpmnModel, String taskKey) {
        FlowNode flowNode = (FlowNode) BpmnModelUtils.getFlowElement(bpmnModel, taskKey);
        FlowElement flowElement = BpmnModelUtils.getFlowElement(bpmnModel, flowNode.getOutgoingFlows().get(0).getTargetRef());
        List<String> sequenceFlowNames = new ArrayList<>();
        if (flowElement instanceof ExclusiveGateway) {
            List<SequenceFlow> flows = ((ExclusiveGateway) flowElement).getOutgoingFlows();
            for (SequenceFlow sequenceFlow : flows) {
                sequenceFlowNames.add(sequenceFlow.getName());
            }
        } else {
            String name = flowNode.getOutgoingFlows().get(0).getName();
            sequenceFlowNames.add(StringUtils.isNotBlank(name) ? name : "提交下一步");
        }

        return sequenceFlowNames;
    }
}
