package com.github.rd.jlv.eclipse.ui.preferences.additional;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.RGB;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.eclipse.ResourceManager;
import com.github.rd.jlv.props.LoglistLevelColor;

public class LevelForegroundColumnCellEditor extends EditingSupport {

	private final ResourceManager resourceManager = JlvActivator.getDefault().getResourceManager();

	private final TableViewer viewer;
	private final CellEditor editor;

	public LevelForegroundColumnCellEditor(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
		editor = new ColorCellEditor(viewer.getTable());
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		LoglistLevelColor level = (LoglistLevelColor) element;
		return resourceManager.toSystemRgb(level.getBackground());
	}

	@Override
	protected void setValue(Object element, Object value) {
		LoglistLevelColor level = (LoglistLevelColor) element;
		level.setBackground(resourceManager.fromSystemRgb((RGB) value));
		viewer.update(level, null);
	}
}
