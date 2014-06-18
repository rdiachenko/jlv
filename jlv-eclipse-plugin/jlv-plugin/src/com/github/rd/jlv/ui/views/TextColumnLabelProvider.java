package com.github.rd.jlv.ui.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.ResourceUtility;
import com.github.rd.jlv.log4j.LogUtil;
import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.pfers.PresentationalModel.ModelItem.Rgb;
import com.github.rd.jlv.ui.preferences.PreferenceManager;

public class TextColumnLabelProvider extends ColumnLabelProvider {

	private static final int TEXT_LENGTH_LIMIT = 200;

	private String fieldName;

	private PreferenceManager preferenceManager;

	public TextColumnLabelProvider(String fieldName) {
		super();
		this.fieldName = fieldName;
		this.preferenceManager = JlvActivator.getDefault().getPreferenceManager();
	}

	@Override
	public String getText(Object element) {
		Log log = (Log) element;
		String value = LogUtil.getValue(log, fieldName);
		value = value.replaceAll("\\r|\\n", " ");

		if (value.length() > TEXT_LENGTH_LIMIT) {
			value = value.substring(0, TEXT_LENGTH_LIMIT) + "...";
		}
		return value;
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public Color getForeground(Object element) {
		Rgb rgb = preferenceManager.getDetailedPrefs().getLogLevelRgb(((Log) element).getLevel());
		return ResourceUtility.getColor(rgb);
	}

	@Override
	public Color getBackground(Object element) {
		Rgb rgb = preferenceManager.getDetailedPrefs().getLogLevelRgb(((Log) element).getLevel(), false);
		return ResourceUtility.getColor(rgb);
	}

	@Override
	public Font getFont(Object element) {
		return ResourceUtility.getFont(preferenceManager.getDetailedPrefs().getFontSize());
	}
}
