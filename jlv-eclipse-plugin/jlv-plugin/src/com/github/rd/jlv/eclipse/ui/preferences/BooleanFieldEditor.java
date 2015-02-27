package com.github.rd.jlv.eclipse.ui.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.github.rd.jlv.props.JlvProperties;
import com.github.rd.jlv.props.PropertyKey;

public class BooleanFieldEditor extends FieldEditor {

	private Button field;
	private String label;
	private boolean value;

	private JlvProperties store;
	private PropertyKey key;
	
	public BooleanFieldEditor(JlvProperties store, PropertyKey key, Composite parent, String label) {
		this.store = store;
		this.key = key;
		this.label = label;
		createControl(parent);
	}
	
	@Override
	protected void adjustForNumColumns(int numColumns) {
		((GridData) field.getLayoutData()).horizontalSpan = numColumns;
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		field = PreferencePageUtils.createCheckBoxControl(parent, label);
		field.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				value = field.getSelection();
			}
		});
	}

	@Override
	protected void doLoad() {
		value = store.load(key);
		field.setSelection(value);
	}

	@Override
	protected void doLoadDefault() {
		value = store.loadDefault(key);
		field.setSelection(value);
	}

	@Override
	protected void doStore() {
		store.save(key, value);
	}

	@Override
	public int getNumberOfControls() {
		return 1; // one control per line in the layout grid
	}
}
