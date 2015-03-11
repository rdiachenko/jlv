package com.github.rd.jlv.eclipse.ui.preferences;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

import com.github.rd.jlv.props.EventScope;
import com.github.rd.jlv.props.PropertyKey;

public class SpinnerFieldEditor extends FieldEditor {

	private Spinner field;
	private String label;
	private int value;
	private PropertyKey key;

	public SpinnerFieldEditor(PropertyKey key, String label, Composite parent) {
		this.key = key;
		this.label = label;
		createControl(parent);
	}

	@Override
	protected void fillIntoGrid(Composite parent) {
		field = PreferencePageUtils.createSpinnerControl(parent, label);
		field.setMinimum(7);
		field.setMaximum(17);
		field.setSelection(11);
		field.setIncrement(1);
		field.setPageIncrement(5);
		field.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				valueChanged();
			}
		});
	}

	@Override
	protected int getGridColumnsNumber() {
		return 1; // one control per line in the layout grid
	}

	@Override
	protected void load() {
		value = getStore().load(key);
		field.setSelection(value);
	}

	@Override
	protected void loadDefault() {
		value = getStore().loadDefault(key);
		field.setSelection(value);
	}

	@Override
	protected void save() {
		int oldValue = getStore().load(key);
		getStore().save(key, value);
		getStore().firePropertyChangeEvent(key, oldValue, value, EventScope.APPLICATION);
	}

	private void valueChanged() {
		if (!field.getText().isEmpty()) {
			int oldValue = value;
			value = Integer.parseInt(field.getText());
			getStore().firePropertyChangeEvent(key, oldValue, value, EventScope.CONFIGURATION);
		}
	}
}
