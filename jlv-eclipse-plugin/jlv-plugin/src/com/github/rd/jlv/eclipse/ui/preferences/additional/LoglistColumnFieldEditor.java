package com.github.rd.jlv.eclipse.ui.preferences.additional;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.github.rd.jlv.eclipse.ui.preferences.FieldEditor;
import com.github.rd.jlv.props.LoglistColumn;
import com.github.rd.jlv.props.PropertyKey;

public class LoglistColumnFieldEditor extends FieldEditor {

	private TableViewer tableViewer;
	private Button upButton;
	private Button downButton;
	private LoglistColumn value;
	private PropertyKey key;

	public LoglistColumnFieldEditor(PropertyKey key, Composite parent) {
		this.key = key;
		createControl(parent);
	}

	@Override
	protected void fillIntoGrid(Composite parent) {
//		tableViewer = getTableViewerControl(parent);
//		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//		tableViewer.getControl().setLayoutData(gridData);
//
//		Composite buttonBox = getButtonBox(parent);
//		gridData = new GridData();
//		gridData.verticalAlignment = GridData.BEGINNING;
//		buttonBox.setLayoutData(gridData);
	}

	@Override
	protected int getGridColumnsNumber() {
		return 2; // Table composite and Button box composite
	}

	@Override
	protected void load() {
		value = getStore().load(key);
	}

	@Override
	protected void loadDefault() {
		value = getStore().loadDefault(key);
	}

	@Override
	protected void save() {
		getStore().save(key, value);
	}
}
