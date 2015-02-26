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

public class IntegerEditor extends FieldEditor {

	private int minValidValue = 0;
	private int maxValidValue = Integer.MAX_VALUE;
	private String errorMessage;

	private Text field;
	private String label;
	private int value;

	private JlvProperties store;
	private PropertyKey key;

	public IntegerEditor(JlvProperties store, PropertyKey key, Composite parent, String label) {
		this.store = store;
		this.key = key;
		this.label = label;
		errorMessage = JFaceResources.getString("IntegerFieldEditor.errorMessage");
		createControl(parent);
	}

	public void setValidRange(int min, int max) {
		minValidValue = min;
		maxValidValue = max;
		errorMessage = JFaceResources.format("IntegerFieldEditor.errorMessageRange", new Object[] { new Integer(min),
				new Integer(max) });
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
