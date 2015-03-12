package com.github.rd.jlv.eclipse.ui.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import com.github.rd.jlv.Log;
import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.eclipse.ResourceManager;
import com.github.rd.jlv.props.JlvProperties;

public class DefaultColumnLabelProvider extends ColumnLabelProvider {

	private String fieldName;

	private JlvProperties store;
	private ResourceManager resourceManager;

	public DefaultColumnLabelProvider(String fieldName) {
		this.fieldName = fieldName;
		store = JlvActivator.getDefault().getStore();
		resourceManager = JlvActivator.getDefault().getResourceManager();
	}

//	@Override
//	public String getText(Object element) {
//		Log log = (Log) element;
//		return LogUtils.getValue(log, fieldName);
//	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

//	@Override
//	public Color getForeground(Object element) {
//		Rgb rgb = preferenceManager.getLogLevelRgb(((Log) element).getLevel());
//		return resourceManager.getColor(rgb);
//	}
//
//	@Override
//	public Color getBackground(Object element) {
//		Rgb rgb = preferenceManager.getLogLevelRgb(((Log) element).getLevel(), false);
//		return resourceManager.getColor(rgb);
//	}
//
//	@Override
//	public Font getFont(Object element) {
//		return resourceManager.getFont(preferenceManager.getFontSize());
//	}
}
