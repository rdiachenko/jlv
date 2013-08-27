package com.rdiachenko.jlv.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.rdiachenko.jlv.JlvActivator;

public class GeneralPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final int LOG_LIST_VIEW_BUFFER_SIZE_MIN = 50;
	private static final int LOG_LIST_VIEW_BUFFER_SIZE_MAX = 100000;

	private static final int LOG_LIST_VIEW_REFRESHING_TIME_MIN = 500;
	private static final int LOG_LIST_VIEW_REFRESHING_TIME_MAX = 60000;

	public GeneralPreferencePage() {
		setPreferenceStore(JlvActivator.getDefault().getPreferenceStore());
		setDescription("Java Logging Viewer General settings");
	}

	@Override
	public void init(IWorkbench workbench) {
		// initialization moved in constructor
	}

	@Override
	protected void createFieldEditors() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 15;
		Composite parent = getFieldEditorParent();
		parent.setLayout(gridLayout);

		Group serverSettingsGroup = createSettingsGroup(parent, "Server settings");
		IntegerFieldEditor serverPortNumberPrefEditor = new IntegerFieldEditor(
				PreferenceManager.JLV_SERVER_PORT_NUMBER,
				"Port number:", getFieldEditorComposite(serverSettingsGroup));
		addField(serverPortNumberPrefEditor);

		BooleanFieldEditor serverAutoStartPrefEditor = new BooleanFieldEditor(
				PreferenceManager.JLV_SERVER_AUTO_START,
				"Automatic start", getFieldEditorComposite(serverSettingsGroup));
		addField(serverAutoStartPrefEditor);

		Group jlvLogListViewSettingsGroup = createSettingsGroup(parent, "JLV log list view settings");
		IntegerFieldEditor jlvLogListViewBufferSizePrefEditor = new IntegerFieldEditor(
				PreferenceManager.JLV_LOG_LIST_VIEW_BUFFER_SIZE,
				"Buffer size (logs):", getFieldEditorComposite(jlvLogListViewSettingsGroup));
		jlvLogListViewBufferSizePrefEditor.setValidRange(LOG_LIST_VIEW_BUFFER_SIZE_MIN, LOG_LIST_VIEW_BUFFER_SIZE_MAX);
		addField(jlvLogListViewBufferSizePrefEditor);

		IntegerFieldEditor jlvLogListViewRefreshingTimePrefEditor = new IntegerFieldEditor(
				PreferenceManager.JLV_LOG_LIST_VIEW_REFRESHING_TIME,
				"Refreshing time (ms):", getFieldEditorComposite(jlvLogListViewSettingsGroup));
		jlvLogListViewRefreshingTimePrefEditor.setValidRange(LOG_LIST_VIEW_REFRESHING_TIME_MIN,
				LOG_LIST_VIEW_REFRESHING_TIME_MAX);
		addField(jlvLogListViewRefreshingTimePrefEditor);
	}

	private Composite getFieldEditorComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.FILL);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		composite.setLayoutData(layoutData);
		return composite;
	}

	private Group createSettingsGroup(Composite parent, String groupName) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(groupName);
		GridLayout layout = new GridLayout();
		group.setLayout(layout);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		group.setLayoutData(layoutData);
		return group;
	}
}
