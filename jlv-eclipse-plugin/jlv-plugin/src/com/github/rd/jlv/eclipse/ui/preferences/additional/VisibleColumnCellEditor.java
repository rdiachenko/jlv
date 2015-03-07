package com.github.rd.jlv.eclipse.ui.preferences.additional;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import com.github.rd.jlv.props.LoglistColumn;

public class VisibleColumnCellEditor extends EditingSupport {

	private final TableViewer viewer;
	private final CellEditor editor;

	public VisibleColumnCellEditor(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
		editor = new CheckboxCellEditor(viewer.getTable(), SWT.CHECK | SWT.CENTER);
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
		return ((LoglistColumn) element).isVisible();
	}

	@Override
	protected void setValue(Object element, Object value) {
		LoglistColumn column = (LoglistColumn) element;
		column.setVisible((boolean) value);
		viewer.update(column, null);
	}
}
