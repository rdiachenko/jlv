package com.github.rd.jlv.ui.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Layout;

public final class PreferencePageUtility {

	public static Composite createFieldEditorComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.FILL);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		composite.setLayoutData(layoutData);
		return composite;
	}

	public static Group createSettingsGroup(Composite parent, String groupName) {
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

	private PreferencePageUtility() {
		throw new IllegalStateException("This is an util class. The object should not be created.");
	}
}
