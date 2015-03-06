package com.github.rd.jlv.eclipse.ui.preferences;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.github.rd.jlv.props.PropertyKey;

public class IntegerFieldEditor extends FieldEditor {

    private int minValidValue;
	private int maxValidValue;
	private String errorMessage;
	private Text field;
	private String label;
	private int value;
	private PropertyKey key;
	
	public IntegerFieldEditor(PropertyKey key, String label, Composite parent, int minValidValue, int maxValidValue) {
		this.key = key;
		this.label = label;
		this.minValidValue = minValidValue;
		this.maxValidValue = maxValidValue;
		errorMessage = JFaceResources.format("IntegerFieldEditor.errorMessageRange",
				new Object[] { new Integer(minValidValue), new Integer(maxValidValue) });
		createControl(parent);
	}

	public IntegerFieldEditor(PropertyKey key, String label, Composite parent) {
		this(key, label, parent, 0, Integer.MAX_VALUE);
	}
	
	public void load() {
		value = getStore().load(key);
		field.setText(String.valueOf(value));
	}

	public void loadDefault() {
		value = getStore().loadDefault(key);
		field.setText(String.valueOf(value));
	}

	public void save() {
		getStore().save(key, value);
	}

	public void fillIntoGrid(Composite parent, int numColumns) {
		field = PreferencePageUtils.createTextFieldControl(parent, label);
		field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				valueChanged();
			}
		});
		field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				valueChanged();
			}
		});
		field.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(final VerifyEvent e) {
				e.doit = isValid(e.text);
			}
		});
	}

	private boolean isValid(String value) {
		return value.matches("[\\d]*");
	}

	private void valueChanged() {
		int value = Integer.parseInt(field.getText());
		if (value >= minValidValue && value <= maxValidValue) {
			this.value = value;
			clearErrorMessage();
		} else {
			showErrorMessage(errorMessage);
		}
	}
}
