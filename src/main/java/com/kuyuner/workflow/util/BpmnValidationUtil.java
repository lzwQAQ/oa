package com.kuyuner.workflow.util;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.validation.ValidationError;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * bpmnmodel校验工具
 * 
 * @author tangzj
 *
 */
public class BpmnValidationUtil {

	/**
	 * 检查流程设计是否符合项目设计规范
	 * 
	 * @param bpmnModel
	 * @return true：符合，false：不符合
	 */
	public static List<String> isValid(BpmnModel bpmnModel) {

		List<String> list = new ArrayList<String>();
		// 检查所有任务是否只有一个出顺序流
		List<UserTask> tasks = BpmnModelUtils.findAllFlowElement(bpmnModel, UserTask.class);
		for (UserTask task : tasks) {
			int outgoingFlows = task.getOutgoingFlows().size();
			if (outgoingFlows != 1) {
				String error = "UserTask[" + task.getName() + "]中不存在或包含多个出顺序流";
				list.add(error);
				break;
			}
		}

		// 所有网关的出顺序流必须要有名称，不然不好做流程方向判断
		List<ExclusiveGateway> exclusiveGateways = BpmnModelUtils.findAllFlowElement(bpmnModel, ExclusiveGateway.class);
		for (ExclusiveGateway gateway : exclusiveGateways) {
			for (SequenceFlow flow : gateway.getOutgoingFlows()) {
				if (StringUtils.isBlank(flow.getName())) {
					String error = "排他网关下的SequenceFlow[" + flow.getId() + "]没有名称";
					list.add(error);
					break;
				}
			}
		}

		// 所有排他网关的出顺序流如果大于等于2，那么必须要设置顺序
//		Set<Integer> integers = new HashSet<>();
//		for (ExclusiveGateway gateway : exclusiveGateways) {
//			List<SequenceFlow> flows = gateway.getOutgoingFlows();
//			if (flows.size() >= 2) {
//				integers.clear();
//				for (SequenceFlow flow : flows) {
//					String json = flow.getDocumentation();
//					ObjectMapper objectMapper = new ObjectMapper();
//					try {
//						FlowSet flowSet = objectMapper.readValue(json, FlowSet.class);
//						if (flowSet.getOrder() >= 1 && integers.add(flowSet.getOrder())) {
//							continue;
//						}
//						String error = "排他网关下的SequenceFlow[" + flow.getId() + "]顺序设置有误";
//						list.add(error);
//						break;
//					} catch (IOException e) {
//						throw new RuntimeException(e);
//					}
//				}
//			}
//		}

		return list;
	}

	/**
	 * 检查流程设计是否符合BPMN 2.0规范
	 * 
	 * @param bpmnModel
	 * @param engineConfigurationImpl
	 * @return
	 */
	public static List<String> isValid(BpmnModel bpmnModel, ProcessEngineConfigurationImpl engineConfigurationImpl) {
		List<ValidationError> errors = engineConfigurationImpl.getProcessValidator().validate(bpmnModel);
		List<String> list = new ArrayList<String>();
		if (errors.size() != 0) {
			for (ValidationError error : errors) {
				list.add(error.getDefaultDescription());
			}
		}
		return list;
	}

}
