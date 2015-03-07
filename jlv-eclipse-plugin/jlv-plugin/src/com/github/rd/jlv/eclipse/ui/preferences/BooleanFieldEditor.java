package com.github.rd.jlv.eclipse.ui.preferences;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.github.rd.jlv.props.EventScope;
import com.github.rd.jlv.props.PropertyKey;

public class BooleanFieldEditor extends FieldEditor {

	private Button field;
	private String label;
	private boolean value;
	private PropertyKey key;

	public BooleanFieldEditor(PropertyKey key, String label, Composite parent) {
		this.key = key;
		this.label = label;
		createControl(parent);
	}

	@Override
	protected void fillIntoGrid(Composite parent) {
		field = PreferencePageUtils.createCheckBoxControl(parent, label);
		field.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
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
		boolean oldValue = getStore().load(key);
		getStore().save(key, value);
		getStore().firePropertyChangeEvent(key, oldValue, value, EventScope.APPLICATION);
	}

	private void valueChanged() {
		boolean oldValue = value;
		value = field.getSelection();
		getStore().firePropertyChangeEvent(key, oldValue, value, EventScope.CONFIGURATION);
	}
}
