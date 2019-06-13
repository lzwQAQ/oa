package com.kuyuner.workflow.engine.image;

import java.awt.Color;

/**
 * 自定义高亮元素颜色
 * 
 * @author tangzj
 *
 */
public class HighLightFlowElement {
	private String id;
	private Color color = Color.RED;

	public HighLightFlowElement() {
	}
	
	public HighLightFlowElement(String id) {
		this.id = id;
	}

	public HighLightFlowElement(String id, Color color) {
		this.id = id;
		this.color = color;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
