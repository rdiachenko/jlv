package com.github.rd.jlv.eclipse.ui.preferences.additional;

import java.util.List;

import org.eclipse.swt.widgets.Composite;

import com.github.rd.jlv.eclipse.ui.preferences.FieldEditor;
import com.github.rd.jlv.props.LoglistLevelColor;
import com.github.rd.jlv.props.PropertyKey;

public class LoglistLevelColorFieldEditor extends FieldEditor {

	private List<LoglistLevelColor> value;
	private PropertyKey key;

	public LoglistLevelColorFieldEditor(PropertyKey key, Composite parent) {
		this.key = key;
		createControl(parent);
	}

	@Override
	protected void fillIntoGrid(Composite parent) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getGridColumnsNumber() {
		return 1;
	}

	@Override
	protected void load() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadDefault() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void save() {
		// TODO Auto-generated method stub

	}
}
