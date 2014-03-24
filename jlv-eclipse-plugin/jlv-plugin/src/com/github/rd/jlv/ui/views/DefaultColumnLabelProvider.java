package com.github.rd.jlv.ui.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.log4j.LogUtil;
import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.ui.preferences.PreferenceManager;

public class DefaultColumnLabelProvider extends ColumnLabelProvider {

	private String fieldName;

	private PreferenceManager preferenceManager;

	public DefaultColumnLabelProvider(String fieldName) {
		super();
		this.fieldName = fieldName;
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
		return preferenceManager.getColor(((Log) element).getLevel(), SWT.FOREGROUND);
	}

	@Override
	public Color getBackground(Object element) {
		return preferenceManager.getColor(((Log) element).getLevel(), SWT.BACKGROUND);
	}

	@Override
	public Font getFont(Object element) {
		return preferenceManager.getFont();
	}
}
