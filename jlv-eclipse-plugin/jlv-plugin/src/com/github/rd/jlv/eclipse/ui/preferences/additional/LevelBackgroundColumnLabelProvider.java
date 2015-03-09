package com.github.rd.jlv.eclipse.ui.preferences.additional;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.eclipse.ResourceManager;
import com.github.rd.jlv.props.LoglistLevelColor;

public class LevelBackgroundColumnLabelProvider extends ColumnLabelProvider {

	private final ResourceManager resourceManager = JlvActivator.getDefault().getResourceManager();

	@Override
	public String getText(Object element) {
		return "The quick brown fox jumps over the lazy dog";
	}

	@Override
	public Color getForeground(Object element) {
		LoglistLevelColor level = (LoglistLevelColor) element;
		return resourceManager.getColor(level.getForeground());
	}

	@Override
	public Color getBackground(Object element) {
		LoglistLevelColor level = (LoglistLevelColor) element;
		return resourceManager.getColor(level.getBackground());
	}
}
