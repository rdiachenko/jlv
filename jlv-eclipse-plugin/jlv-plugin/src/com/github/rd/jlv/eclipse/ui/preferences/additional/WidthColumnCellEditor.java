package com.github.rd.jlv.eclipse.ui.preferences.additional;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

import com.github.rd.jlv.props.LoglistColumn;

public class WidthColumnCellEditor extends EditingSupport {

	private TableViewer viewer;
	private final CellEditor editor;

	public WidthColumnCellEditor(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;

		editor = new TextCellEditor(viewer.getTable());
		((Text) editor.getControl()).addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				e.doit = e.text.matches("[\\d]*");
			}
		});
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
		return String.valueOf(((LoglistColumn) element).getWidth());
	}

	@Override
	protected void setValue(Object element, Object value) {
		LoglistColumn oldColumn = (LoglistColumn) element;
		int width = Integer.parseInt(String.valueOf(value));
		LoglistColumn newColumn = new LoglistColumn(oldColumn.getName(), oldColumn.isVisible(), width);
		viewer.update(newColumn, null);
	}
}
