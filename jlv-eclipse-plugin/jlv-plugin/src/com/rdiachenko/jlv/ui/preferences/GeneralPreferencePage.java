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

	private static final int LOG_VIEW_BUFFER_SIZE_MIN = 50;
	private static final int LOG_VIEW_BUFFER_SIZE_MAX = 100000;

	private static final int LOG_VIEW_REFRESHING_TIME_MIN = 500;
	private static final int LOG_VIEW_REFRESHING_TIME_MAX = 60000;

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

		Group mainLogViewSettingsGroup = createSettingsGroup(parent, "Main logview settings");
		IntegerFieldEditor logViewBufferSizePrefEditor = new IntegerFieldEditor(
				PreferenceManager.JLV_LOGVIEW_BUFFER_SIZE,
				"Buffer size (logs):", getFieldEditorComposite(mainLogViewSettingsGroup));
		logViewBufferSizePrefEditor.setValidRange(LOG_VIEW_BUFFER_SIZE_MIN, LOG_VIEW_BUFFER_SIZE_MAX);
		addField(logViewBufferSizePrefEditor);

		IntegerFieldEditor logViewRefreshingTimePrefEditor = new IntegerFieldEditor(
				PreferenceManager.JLV_LOGVIEW_REFRESHING_TIME,
				"Refreshing time (ms):", getFieldEditorComposite(mainLogViewSettingsGroup));
		logViewRefreshingTimePrefEditor.setValidRange(LOG_VIEW_REFRESHING_TIME_MIN, LOG_VIEW_REFRESHING_TIME_MAX);
		addField(logViewRefreshingTimePrefEditor);
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
