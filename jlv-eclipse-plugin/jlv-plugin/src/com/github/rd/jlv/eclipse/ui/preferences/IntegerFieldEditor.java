package com.github.rd.jlv.eclipse.ui.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.github.rd.jlv.props.JlvProperties;
import com.github.rd.jlv.props.PropertyKey;

public class IntegerFieldEditor extends FieldEditor {

	private int minValidValue;
	private int maxValidValue;
	private String errorMessage;

	private Text field;
	private String label;
	private int value;

	private JlvProperties store;
	private PropertyKey key;

	public IntegerFieldEditor(JlvProperties store, PropertyKey key, Composite parent, String label, int minValidValue,
			int maxValidValue) {
		this.store = store;
		this.key = key;
		this.label = label;
		this.minValidValue = minValidValue;
		this.maxValidValue = maxValidValue;
		errorMessage = JFaceResources.format("IntegerFieldEditor.errorMessageRange",
				new Object[] { new Integer(minValidValue), new Integer(maxValidValue) });
		createControl(parent);
	}

	public IntegerFieldEditor(JlvProperties store, PropertyKey key, Composite parent, String label) {
		this(store, key, parent, label, 0, Integer.MAX_VALUE);
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		((GridData) field.getLayoutData()).horizontalSpan = numColumns;

	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
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

	@Override
	protected void doLoad() {
		value = store.load(key);
		field.setText(String.valueOf(value));
	}

	@Override
	protected void doLoadDefault() {
		value = store.loadDefault(key);
		field.setText(String.valueOf(value));
	}

	@Override
	protected void doStore() {
		store.save(key, value);
	}

	@Override
	public int getNumberOfControls() {
		return 1; // one control per line in the layout grid
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
