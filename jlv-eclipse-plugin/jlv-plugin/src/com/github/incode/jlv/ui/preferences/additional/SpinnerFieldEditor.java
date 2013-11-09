package com.github.incode.jlv.ui.preferences.additional;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

import com.github.incode.jlv.JlvActivator;
import com.google.common.base.Strings;

public class SpinnerFieldEditor extends FieldEditor {

//	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static enum Style {
		LABEL_BEFORE, LABEL_AFTER
	};

	private Style style;

	private Spinner spinner;

	private int value = 11;

	private IPreferenceStore store;

	public SpinnerFieldEditor(String name, Composite parent) {
		this(name, "", parent);
	}

	public SpinnerFieldEditor(String name, String labelText, Composite parent) {
		this(name, labelText, Style.LABEL_AFTER, parent);
	}

	public SpinnerFieldEditor(String name, String labelText, Style style, Composite parent) {
		init(name, labelText);
		this.style = style;
		this.store = JlvActivator.getDefault().getPreferenceStore();
		createControl(parent);
	}

	public int getIntValue() {
		return value;
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		((GridData) spinner.getLayoutData()).horizontalSpan = numColumns;
	}

	@Override
	public int getNumberOfControls() {
		return 2; // 2 controls: label and spinner
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		switch (style) {
		case LABEL_AFTER:
			spinner = getSpinnerControl(parent);
			getLabelControl(parent);
			break;
		default:
			getLabelControl(parent);
			spinner = getSpinnerControl(parent);
		}
	}

	@Override
	protected void doLoad() {
		if (spinner != null) {
			value = store.getInt(getPreferenceName());
			spinner.setSelection(value);
		}
	}

	@Override
	protected void doLoadDefault() {
		if (spinner != null) {
			value = store.getDefaultInt(getPreferenceName());
			spinner.setSelection(value);
		}
	}

	@Override
	protected void doStore() {
		if (spinner != null) {
			store.setValue(getPreferenceName(), value);
		}
	}

	private void valueChanged(int oldValue, int newValue) {
		setPresentsDefaultValue(false);

		if (oldValue != newValue) {
			fireValueChanged(VALUE, oldValue, newValue);
		}
	}

	private Spinner getSpinnerControl(Composite parent) {
		if (spinner == null) {
			spinner = new Spinner(parent, SWT.BORDER);
			spinner.setMinimum(7);
			spinner.setMaximum(17);
			spinner.setSelection(value);
			spinner.setIncrement(1);
			spinner.setPageIncrement(5);

			spinner.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(final ModifyEvent e) {
					Spinner spinner = (Spinner) e.getSource();
					String stringValue = spinner.getText();

					if (!Strings.isNullOrEmpty(stringValue)) {
						int newValue = Integer.parseInt(stringValue);
						valueChanged(value, newValue);
						value = Integer.parseInt(stringValue);
					}
				}
			});

			spinner.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent event) {
					spinner = null;
				}
			});
		} else {
			checkParent(spinner, parent);
		}
		return spinner;
	}
}
