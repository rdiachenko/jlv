package com.github.rd.jlv.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.github.rd.jlv.JlvActivator;

public class GeneralPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final int LOG_LIST_VIEW_BUFFER_SIZE_MIN = 50; // log's number
	private static final int LOG_LIST_VIEW_BUFFER_SIZE_MAX = 100000; // log's number

	private static final int LOG_LIST_VIEW_REFRESHING_TIME_MIN = 500; // ms
	private static final int LOG_LIST_VIEW_REFRESHING_TIME_MAX = 60000; // ms

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(JlvActivator.getDefault().getPreferenceStore());
		setDescription("General settings");
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		parent.setLayout(PreferencePageUtils.createFieldEditorParentLayout());

		Group serverSettingsGroup = PreferencePageUtils.createSettingsGroup(parent, "Server settings");
		IntegerFieldEditor serverPortEditor = new IntegerFieldEditor(
				PreferenceManager.SERVER_PORT_NUMBER,
				"Port number:",
				PreferencePageUtils.createFieldEditorComposite(serverSettingsGroup));
		addField(serverPortEditor);

		BooleanFieldEditor serverStartEditor = new BooleanFieldEditor(
				PreferenceManager.SERVER_AUTO_START,
				"Automatic start",
				PreferencePageUtils.createFieldEditorComposite(serverSettingsGroup));
		addField(serverStartEditor);

		Group logListViewSettingsGroup = PreferencePageUtils.createSettingsGroup(parent,
				"JLV log list view settings");
		IntegerFieldEditor bufferSizeEditor = new IntegerFieldEditor(
				PreferenceManager.LOGS_BUFFER_SIZE,
				"Buffer size (logs):",
				PreferencePageUtils.createFieldEditorComposite(logListViewSettingsGroup));
		bufferSizeEditor.setValidRange(LOG_LIST_VIEW_BUFFER_SIZE_MIN, LOG_LIST_VIEW_BUFFER_SIZE_MAX);
		addField(bufferSizeEditor);

		IntegerFieldEditor refreshingTimeEditor = new IntegerFieldEditor(
				PreferenceManager.LOGS_REFRESHING_TIME,
				"Refreshing time (ms):",
				PreferencePageUtils.createFieldEditorComposite(logListViewSettingsGroup));
		refreshingTimeEditor.setValidRange(LOG_LIST_VIEW_REFRESHING_TIME_MIN,
				LOG_LIST_VIEW_REFRESHING_TIME_MAX);
		addField(refreshingTimeEditor);
	}
}
