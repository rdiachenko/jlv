package com.github.rd.jlv.eclipse.ui.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.eclipse.ResourceManager;
import com.github.rd.jlv.eclipse.ui.preferences.PreferenceManager;

public class DefaultColumnLabelProvider extends ColumnLabelProvider {

	private String fieldName;

	private PreferenceManager preferenceManager;

	private ResourceManager resourceManager;

	public DefaultColumnLabelProvider(String fieldName) {
		super();
		this.fieldName = fieldName;
		preferenceManager = JlvActivator.getDefault().getPreferenceManager();
		resourceManager = JlvActivator.getDefault().getResourceManager();
	}

//	@Override
//	public String getText(Object element) {
//		Log log = (Log) element;
//		return LogUtils.getValue(log, fieldName);
//	}
//
//	@Override
//	public Image getImage(Object element) {
//		return null;
//	}
//
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
