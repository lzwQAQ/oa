/**
 * 
 * 校验表单等相关操作
 * 
 * @param callBack 结果回调函数
 */
function validateForm(callBack){
	var validateResult=false;//校验结果
	var auditResult="同意";//审批结果（流程图中，顺序流上面的字，通常有“同意”或“不同意”等）
	//TODO 表单校验或其他操作,校验通过validateResult设置为true，否则设置为false
	
	//结果回调
	callBack(validateResult,auditResult);
}

/**
 * 保存业务信息，完成当前环节
 * 
 * @param task 任务信息，json字符串
 * @param isOuter 是否为外网模式。外网：true；内网：false
 * @param callBack 结果回调函数
 */
function submitForm(task,isOuter,callBack){
	var submitResult=false;
	//TODO 使用ajax进行异步请求,进行数据入库等操作。需要将task字符串同时提交到后台，作为调用api的参数
	//TODO 请求完成后调用回调方法,提交成功submitResult设置为true，否则设置为false
	
	//结果回调
	if(isOuter){
		callBack(submitResult,function(){
			//外网的时候，需要在回调方法中传入回调方法，用于处理个性化的业务需求。内网的时候该参数无效
		});
	}else{
		callBack(submitResult);
	}
}

/**
 * 
 * 仅保存业务数据
 * 
 */
function saveForm(modelKey,sequenceFlowName){
	//TODO 该方法应该仅仅用于保存业务数据，不应该执行完成任务等操作。
	//TODO 保存业务数据的时候应该modelKey和sequenceFlowName这两个字段一起保存起来。
}