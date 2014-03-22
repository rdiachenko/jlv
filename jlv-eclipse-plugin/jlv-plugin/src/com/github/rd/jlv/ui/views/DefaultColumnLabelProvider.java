package com.github.rd.jlv.ui.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.ResourceManager;
import com.github.rd.jlv.log4j.LogUtil;
import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.ui.preferences.PreferenceManager;

public class DefaultColumnLabelProvider extends ColumnLabelProvider {

	private String fieldName;

	private ResourceManager resourceManager;

	private PreferenceManager preferenceManager;

	public DefaultColumnLabelProvider(String fieldName) {
		super();
		this.fieldName = fieldName;
		this.resourceManager = JlvActivator.getDefault().getResourceManager();
		this.preferenceManager = JlvActivator.getDefault().getPreferenceManager();
	}

	@Override
	public String getText(Object element) {
		Log log = (Log) element;
		return LogUtil.getValue(log, fieldName);
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public Color getForeground(Object element) {
		Display display = Display.getCurrent();

		if (display == null) {
			return null;
		} else {
			Log log = (Log) element;
			RGB rgb = preferenceManager.getForeground(log.getLevel());
			return resourceManager.getColor(display, rgb);
		}
	}

	@Override
	public Color getBackground(Object element) {
		Display display = Display.getCurrent();

		if (display == null) {
			return null;
		} else {
			Log log = (Log) element;
			RGB rgb = preferenceManager.getBackground(log.getLevel());
			return resourceManager.getColor(display, rgb);
		}
	}

	@Override
	public Font getFont(Object element) {
		Display display = Display.getCurrent();

		if (display == null) {
			return null;
		} else {
			return resourceManager.getFont(display, preferenceManager.getFontSize());
		}
	}
}
