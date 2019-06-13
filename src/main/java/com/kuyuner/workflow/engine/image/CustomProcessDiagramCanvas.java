package com.kuyuner.workflow.engine.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

import org.activiti.bpmn.model.AssociationDirection;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.image.impl.DefaultProcessDiagramCanvas;

/**
 * 自定义画布，解决task不能正常显示字体，sequenceflow不显示字体的问题
 * 
 * @author tangzj
 *
 */
public class CustomProcessDiagramCanvas extends DefaultProcessDiagramCanvas {

	public CustomProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType) {
		super(width, height, minX, minY, imageType);
		init();
	}

	public CustomProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType,
			String activityFontName, String labelFontName, ClassLoader customClassLoader) {
		super(width, height, minX, minY, imageType, activityFontName, labelFontName, customClassLoader);
		init();
	}

	private void init() {
		LABEL_COLOR = new Color(0, 0, 0);
		LABEL_FONT = new Font(labelFontName, Font.ITALIC, 11);
	}

	@Override
	public void drawHighLight(int x, int y, int width, int height) {
		drawHighLight(x, y, width, height, HIGHLIGHT_COLOR);
	}

	/**
	 * 该方法在原有方法基础上添加了Color参数，用于自定义高亮颜色
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public void drawHighLight(int x, int y, int width, int height, Color color) {
		Paint originalPaint = g.getPaint();
		Stroke originalStroke = g.getStroke();

		g.setPaint(color);
		g.setStroke(THICK_TASK_BORDER_STROKE);

		RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 6, 6);// TODO
																						// 减小矩形圆角
		g.draw(rect);

		g.setPaint(originalPaint);
		g.setStroke(originalStroke);
	}

	@Override
	protected void drawTask(String name, GraphicInfo graphicInfo, boolean thickBorder) {
		Paint originalPaint = g.getPaint();
		int x = (int) graphicInfo.getX();
		int y = (int) graphicInfo.getY();
		int width = (int) graphicInfo.getWidth();
		int height = (int) graphicInfo.getHeight();

		// Create a new gradient paint for every task box, gradient depends on x
		// and y and is not relative
		g.setPaint(TASK_BOX_COLOR);

		int arcR = 6;
		if (thickBorder){
			arcR = 3;
		}

		// shape
		RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, arcR, arcR);
		g.fill(rect);
		g.setPaint(TASK_BORDER_COLOR);

		if (thickBorder) {
			Stroke originalStroke = g.getStroke();
			g.setStroke(THICK_TASK_BORDER_STROKE);
			g.draw(rect);
			g.setStroke(originalStroke);
		} else {
			g.draw(rect);
		}

		g.setPaint(originalPaint);
		// text
		if (name != null && name.length() > 0) {
			// 直接放字体的矩形宽高等于任务框的宽高，让字体居中显示，防止因为任务框过小导致字体不能显示
			drawMultilineCentredText(name, x, y, width, height);
		}
	}

	/**
	 * 自定义高亮颜色
	 * 
	 * @param xPoints
	 * @param yPoints
	 * @param conditional
	 * @param isDefault
	 * @param connectionType
	 * @param associationDirection
	 * @param highLighted
	 * @param highLightColor
	 * @param scaleFactor
	 */
	public void drawConnection(int[] xPoints, int[] yPoints, boolean conditional, boolean isDefault,
			String connectionType, AssociationDirection associationDirection, boolean highLighted, Color highLightColor,
			double scaleFactor) {

		Paint originalPaint = g.getPaint();
		Stroke originalStroke = g.getStroke();

		g.setPaint(CONNECTION_COLOR);
		if (connectionType.equals("association")) {
			g.setStroke(ASSOCIATION_STROKE);
		} else if (highLighted) {
			g.setPaint(highLightColor);
			g.setStroke(HIGHLIGHT_FLOW_STROKE);
		}

		for (int i = 1; i < xPoints.length; i++) {
			Integer sourceX = xPoints[i - 1];
			Integer sourceY = yPoints[i - 1];
			Integer targetX = xPoints[i];
			Integer targetY = yPoints[i];
			Line2D.Double line = new Line2D.Double(sourceX, sourceY, targetX, targetY);
			g.draw(line);
		}

		if (isDefault) {
			Line2D.Double line = new Line2D.Double(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
			drawDefaultSequenceFlowIndicator(line, scaleFactor);
		}

		if (conditional) {
			Line2D.Double line = new Line2D.Double(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
			drawConditionalSequenceFlowIndicator(line, scaleFactor);
		}

		if (associationDirection.equals(AssociationDirection.ONE)
				|| associationDirection.equals(AssociationDirection.BOTH)) {
			Line2D.Double line = new Line2D.Double(xPoints[xPoints.length - 2], yPoints[xPoints.length - 2],
					xPoints[xPoints.length - 1], yPoints[xPoints.length - 1]);
			drawArrowHead(line, scaleFactor);
		}
		if (associationDirection.equals(AssociationDirection.BOTH)) {
			Line2D.Double line = new Line2D.Double(xPoints[1], yPoints[1], xPoints[0], yPoints[0]);
			drawArrowHead(line, scaleFactor);
		}
		g.setPaint(originalPaint);
		g.setStroke(originalStroke);
	}

}
