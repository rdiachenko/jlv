package com.github.rd.jlv.eclipse.ui.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public final class PreferencePageUtils {

	public static Composite createCompositeGroup(Composite parent, String groupName) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(groupName);
		GridLayout layout = new GridLayout();
		group.setLayout(layout);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		group.setLayoutData(layoutData);
		return group;
	}

	public static Layout createFieldEditorParentLayout() {
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 15;
		return layout;
	}

	public static Button createCheckBoxControl(Composite parent, String name) {
		Composite composite = new Composite(parent, SWT.FILL);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);

		GridData layoutData = new GridData(SWT.BEGINNING, SWT.NONE, true, false);
		composite.setLayoutData(layoutData);

		Button checkBoxControl = new Button(composite, SWT.CHECK);
		checkBoxControl.setText(name);
		return checkBoxControl;
	}

	public static Text createTextFieldControl(Composite parent, String name) {
		Composite composite = new Composite(parent, SWT.FILL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		composite.setLayoutData(layoutData);

		Label label = new Label(composite, SWT.NONE);
		label.setText(name);

		Text textField = new Text(composite, SWT.BORDER);
		layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		textField.setLayoutData(layoutData);
		return textField;
	}

	public static Spinner createSpinnerControl(Composite parent, String name) {
		Composite composite = new Composite(parent, SWT.FILL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		composite.setLayoutData(layoutData);

		Spinner spinner = new Spinner(composite, SWT.BORDER);
		layoutData = new GridData();
		layoutData.widthHint = 20;
		spinner.setLayoutData(layoutData);

		Label label = new Label(composite, SWT.NONE);
		label.setText(name);
		return spinner;
	}

	private PreferencePageUtils() {
		throw new IllegalStateException("This is an util class. The object should not be created.");
	}
}
