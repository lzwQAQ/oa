package com.kuyuner.workflow.engine.image;

import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.image.impl.DefaultProcessDiagramCanvas;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 调用自定义画布{@link CustomProcessDiagramCanvas }，解决不现实字体问题
 *
 * @author tangzj
 */
public class CustomProcessDiagramGenerator extends DefaultProcessDiagramGenerator {

    private static final double SCALE_FACTOR = 1.0;

    public CustomProcessDiagramGenerator() {
        super();
    }

    public CustomProcessDiagramGenerator(double scaleFactor) {
        super(scaleFactor);
    }

    /**
     * 自定义高亮颜色,每一个流程元素都可以自定义颜色
     *
     * @param bpmnModel
     * @param imageType
     * @param highLightedActivities
     * @param highLightedFlows
     * @param activityFontName
     * @param labelFontName
     * @param customClassLoader
     * @return
     */
    public InputStream generateDiagram(BpmnModel bpmnModel, String imageType,
                                       List<HighLightFlowElement> highLightedActivities, List<HighLightFlowElement> highLightedFlows,
                                       String activityFontName, String labelFontName, ClassLoader customClassLoader) {
        List<String> highLightedActivitiesList = getFlowElementIds(highLightedActivities);
        List<String> highLightedFlowsList = getFlowElementIds(highLightedFlows);
        CustomProcessDiagramCanvas customProcessDiagramCanvas = (CustomProcessDiagramCanvas) generateProcessDiagram(
                bpmnModel, imageType, highLightedActivitiesList, highLightedFlowsList, activityFontName, labelFontName,
                customClassLoader, SCALE_FACTOR);

        // Draw highlighted activities
        for (HighLightFlowElement flowElement : highLightedActivities) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowElement.getId());
            customProcessDiagramCanvas.drawHighLight((int) graphicInfo.getX(), (int) graphicInfo.getY(),
                    (int) graphicInfo.getWidth(), (int) graphicInfo.getHeight(), flowElement.getColor());
        }

        // Draw highlighted sequenceFlow
        for (HighLightFlowElement flowElement : highLightedFlows) {
            boolean conditional = false;
            List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(flowElement.getId());
            if (graphicInfoList != null && graphicInfoList.size() > 0) {
                SequenceFlow sequenceFlow = (SequenceFlow) bpmnModel.getFlowElement(flowElement.getId());
                String sourceRef = sequenceFlow.getSourceRef();
                String targetRef = sequenceFlow.getTargetRef();
                FlowElement sourceElement = bpmnModel.getFlowElement(sourceRef);
                FlowElement targetElement = bpmnModel.getFlowElement(targetRef);

                graphicInfoList = connectionPerfectionizer(customProcessDiagramCanvas, bpmnModel, sourceElement,
                        targetElement, graphicInfoList);
                int xPoints[] = new int[graphicInfoList.size()];
                int yPoints[] = new int[graphicInfoList.size()];

                for (int i = 1; i < graphicInfoList.size(); i++) {
                    GraphicInfo graphicInfo = graphicInfoList.get(i);
                    GraphicInfo previousGraphicInfo = graphicInfoList.get(i - 1);

                    if (i == 1) {
                        xPoints[0] = (int) previousGraphicInfo.getX();
                        yPoints[0] = (int) previousGraphicInfo.getY();
                    }
                    xPoints[i] = (int) graphicInfo.getX();
                    yPoints[i] = (int) graphicInfo.getY();

                }

                customProcessDiagramCanvas.drawConnection(xPoints, yPoints, conditional, false, "sequenceFlow",
                        AssociationDirection.ONE, true, flowElement.getColor(), SCALE_FACTOR);
            }
        }

        return customProcessDiagramCanvas.generateImage(imageType);
    }

    @Override
    protected void drawActivity(DefaultProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel,
                                FlowNode flowNode, List<String> highLightedActivities, List<String> highLightedFlows, double scaleFactor) {

        ActivityDrawInstruction drawInstruction = activityDrawInstructions.get(flowNode.getClass());
        if (drawInstruction != null) {

            drawInstruction.draw(processDiagramCanvas, bpmnModel, flowNode);

            // Gather info on the multi instance marker
            boolean multiInstanceSequential = false, multiInstanceParallel = false, collapsed = false;
            if (flowNode instanceof Activity) {
                Activity activity = (Activity) flowNode;
                MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = activity.getLoopCharacteristics();
                if (multiInstanceLoopCharacteristics != null) {
                    multiInstanceSequential = multiInstanceLoopCharacteristics.isSequential();
                    multiInstanceParallel = !multiInstanceSequential;
                }
            }

            // Gather info on the collapsed marker
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            if (flowNode instanceof SubProcess) {
                collapsed = graphicInfo.getExpanded() != null && !graphicInfo.getExpanded();
            } else if (flowNode instanceof CallActivity) {
                collapsed = true;
            }

            if (scaleFactor == 1.0) {
                // Actually draw the markers
                processDiagramCanvas.drawActivityMarkers((int) graphicInfo.getX(), (int) graphicInfo.getY(),
                        (int) graphicInfo.getWidth(), (int) graphicInfo.getHeight(), multiInstanceSequential,
                        multiInstanceParallel, collapsed);
            }

            // Draw highlighted activities
            if (highLightedActivities.contains(flowNode.getId())) {
                drawHighLight(processDiagramCanvas, bpmnModel.getGraphicInfo(flowNode.getId()));
            }

        }

        // Outgoing transitions of activity
        for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
            boolean highLighted = (highLightedFlows.contains(sequenceFlow.getId()));
            String defaultFlow = null;
            if (flowNode instanceof Activity) {
                defaultFlow = ((Activity) flowNode).getDefaultFlow();
            } else if (flowNode instanceof Gateway) {
                defaultFlow = ((Gateway) flowNode).getDefaultFlow();
            }

            boolean isDefault = false;
            if (defaultFlow != null && defaultFlow.equalsIgnoreCase(sequenceFlow.getId())) {
                isDefault = true;
            }
            boolean drawConditionalIndicator = sequenceFlow.getConditionExpression() != null
                    && !(flowNode instanceof Gateway);

            String sourceRef = sequenceFlow.getSourceRef();
            String targetRef = sequenceFlow.getTargetRef();
            FlowElement sourceElement = bpmnModel.getFlowElement(sourceRef);
            FlowElement targetElement = bpmnModel.getFlowElement(targetRef);
            List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
            if (graphicInfoList != null && graphicInfoList.size() > 0) {
                graphicInfoList = connectionPerfectionizer(processDiagramCanvas, bpmnModel, sourceElement,
                        targetElement, graphicInfoList);
                int xPoints[] = new int[graphicInfoList.size()];
                int yPoints[] = new int[graphicInfoList.size()];

                for (int i = 1; i < graphicInfoList.size(); i++) {
                    GraphicInfo graphicInfo = graphicInfoList.get(i);
                    GraphicInfo previousGraphicInfo = graphicInfoList.get(i - 1);

                    if (i == 1) {
                        xPoints[0] = (int) previousGraphicInfo.getX();
                        yPoints[0] = (int) previousGraphicInfo.getY();
                    }
                    xPoints[i] = (int) graphicInfo.getX();
                    yPoints[i] = (int) graphicInfo.getY();

                }

                processDiagramCanvas.drawSequenceflow(xPoints, yPoints, drawConditionalIndicator, isDefault,
                        highLighted, scaleFactor);

                // Draw sequenceflow label
                GraphicInfo labelGraphicInfo = bpmnModel.getLabelGraphicInfo(sequenceFlow.getId());
                if (labelGraphicInfo != null) {
                    processDiagramCanvas.drawLabel(sequenceFlow.getName(), labelGraphicInfo, false);
                } else {
                    // TODO 没有label就默认线的中间
                    if (graphicInfoList != null && graphicInfoList.size() > 0) {
                        processDiagramCanvas.drawLabel(sequenceFlow.getName(), getLineCenter(graphicInfoList),
                                isCenter(graphicInfoList));
                    }
                }
            }
        }

        // Nested elements
        if (flowNode instanceof FlowElementsContainer) {
            for (FlowElement nestedFlowElement : ((FlowElementsContainer) flowNode).getFlowElements()) {
                if (nestedFlowElement instanceof FlowNode) {
                    drawActivity(processDiagramCanvas, bpmnModel, (FlowNode) nestedFlowElement, highLightedActivities,
                            highLightedFlows, scaleFactor);
                }
            }
        }
    }

    @Override
    protected DefaultProcessDiagramCanvas generateProcessDiagram(BpmnModel bpmnModel, String imageType,
                                                                 List<String> highLightedActivities, List<String> highLightedFlows, String activityFontName,
                                                                 String labelFontName, ClassLoader customClassLoader, double scaleFactor) {

        prepareBpmnModel(bpmnModel);

        DefaultProcessDiagramCanvas processDiagramCanvas = initProcessDiagramCanvas(bpmnModel, imageType,
                activityFontName, labelFontName, customClassLoader);

        // Draw pool shape, if process is participant in collaboration
        for (Pool pool : bpmnModel.getPools()) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            processDiagramCanvas.drawPoolOrLane(pool.getName(), graphicInfo);
        }

        // Draw lanes
        for (Process process : bpmnModel.getProcesses()) {
            for (Lane lane : process.getLanes()) {
                GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(lane.getId());
                processDiagramCanvas.drawPoolOrLane(lane.getName(), graphicInfo);
            }
        }

        // Draw activities and their sequence-flows
        for (FlowNode flowNode : bpmnModel.getProcesses().get(0).findFlowElementsOfType(FlowNode.class)) {
            drawActivity(processDiagramCanvas, bpmnModel, flowNode, highLightedActivities, highLightedFlows,
                    scaleFactor);
        }

        // Draw artifacts
        for (Process process : bpmnModel.getProcesses()) {
            for (Artifact artifact : process.getArtifacts()) {
                drawArtifact(processDiagramCanvas, bpmnModel, artifact);
            }
        }

        return processDiagramCanvas;
    }

    protected static DefaultProcessDiagramCanvas initProcessDiagramCanvas(BpmnModel bpmnModel, String imageType,
                                                                          String activityFontName, String labelFontName, ClassLoader customClassLoader) {

        // We need to calculate maximum values to know how big the image will be
        // in its entirety
        double minX = Double.MAX_VALUE;
        double maxX = 0;
        double minY = Double.MAX_VALUE;
        double maxY = 0;

        for (Pool pool : bpmnModel.getPools()) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            minX = graphicInfo.getX();
            maxX = graphicInfo.getX() + graphicInfo.getWidth();
            minY = graphicInfo.getY();
            maxY = graphicInfo.getY() + graphicInfo.getHeight();
        }

        List<FlowNode> flowNodes = gatherAllFlowNodes(bpmnModel);
        for (FlowNode flowNode : flowNodes) {

            GraphicInfo flowNodeGraphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());

            // width
            if (flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth() > maxX) {
                maxX = flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth();
            }
            if (flowNodeGraphicInfo.getX() < minX) {
                minX = flowNodeGraphicInfo.getX();
            }
            // height
            if (flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight() > maxY) {
                maxY = flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight();
            }
            if (flowNodeGraphicInfo.getY() < minY) {
                minY = flowNodeGraphicInfo.getY();
            }

            for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
                List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
                if (graphicInfoList != null) {
                    for (GraphicInfo graphicInfo : graphicInfoList) {
                        // width
                        if (graphicInfo.getX() > maxX) {
                            maxX = graphicInfo.getX();
                        }
                        if (graphicInfo.getX() < minX) {
                            minX = graphicInfo.getX();
                        }
                        // height
                        if (graphicInfo.getY() > maxY) {
                            maxY = graphicInfo.getY();
                        }
                        if (graphicInfo.getY() < minY) {
                            minY = graphicInfo.getY();
                        }
                    }
                }
            }
        }

        List<Artifact> artifacts = gatherAllArtifacts(bpmnModel);
        for (Artifact artifact : artifacts) {

            GraphicInfo artifactGraphicInfo = bpmnModel.getGraphicInfo(artifact.getId());

            if (artifactGraphicInfo != null) {
                // width
                if (artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth() > maxX) {
                    maxX = artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth();
                }
                if (artifactGraphicInfo.getX() < minX) {
                    minX = artifactGraphicInfo.getX();
                }
                // height
                if (artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight() > maxY) {
                    maxY = artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight();
                }
                if (artifactGraphicInfo.getY() < minY) {
                    minY = artifactGraphicInfo.getY();
                }
            }

            List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(artifact.getId());
            if (graphicInfoList != null) {
                for (GraphicInfo graphicInfo : graphicInfoList) {
                    // width
                    if (graphicInfo.getX() > maxX) {
                        maxX = graphicInfo.getX();
                    }
                    if (graphicInfo.getX() < minX) {
                        minX = graphicInfo.getX();
                    }
                    // height
                    if (graphicInfo.getY() > maxY) {
                        maxY = graphicInfo.getY();
                    }
                    if (graphicInfo.getY() < minY) {
                        minY = graphicInfo.getY();
                    }
                }
            }
        }

        int nrOfLanes = 0;
        for (Process process : bpmnModel.getProcesses()) {
            for (Lane l : process.getLanes()) {

                nrOfLanes++;

                GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(l.getId());
                // // width
                if (graphicInfo.getX() + graphicInfo.getWidth() > maxX) {
                    maxX = graphicInfo.getX() + graphicInfo.getWidth();
                }
                if (graphicInfo.getX() < minX) {
                    minX = graphicInfo.getX();
                }
                // height
                if (graphicInfo.getY() + graphicInfo.getHeight() > maxY) {
                    maxY = graphicInfo.getY() + graphicInfo.getHeight();
                }
                if (graphicInfo.getY() < minY) {
                    minY = graphicInfo.getY();
                }
            }
        }

        // Special case, see https://activiti.atlassian.net/browse/ACT-1431
        if (flowNodes.isEmpty() && bpmnModel.getPools().isEmpty() && nrOfLanes == 0) {
            // Nothing to show
            minX = 0;
            minY = 0;
        }

        // TODO 使用自定义画布
        return new CustomProcessDiagramCanvas((int) maxX + 80, (int) maxY + 30, (int) minX, (int) minY, imageType,
                activityFontName, labelFontName, customClassLoader);
    }

    /**
     * 判断是否需要在中间，仅在横线的时候居中
     *
     * @param graphicInfoList
     * @return
     */
    private boolean isCenter(List<GraphicInfo> graphicInfoList) {
        if (graphicInfoList.size() == 2) {
            return graphicInfoList.get(0).getY() == graphicInfoList.get(1).getY();
        }
        return false;
    }

    private static void drawHighLight(DefaultProcessDiagramCanvas processDiagramCanvas, GraphicInfo graphicInfo) {
        processDiagramCanvas.drawHighLight((int) graphicInfo.getX(), (int) graphicInfo.getY(),
                (int) graphicInfo.getWidth(), (int) graphicInfo.getHeight());
    }

    private List<String> getFlowElementIds(List<HighLightFlowElement> highLightFlowElements) {
        List<String> list = new ArrayList<String>();
        for (HighLightFlowElement element : highLightFlowElements) {
            list.add(element.getId());
        }
        return list;
    }

}
