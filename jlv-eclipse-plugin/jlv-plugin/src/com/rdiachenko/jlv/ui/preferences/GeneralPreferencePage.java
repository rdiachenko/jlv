package com.rdiachenko.jlv.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.rdiachenko.jlv.JlvActivator;

public class GeneralPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final int LOG_VIEW_BUFFER_SIZE_MIN = 50;
	private static final int LOG_VIEW_BUFFER_SIZE_MAX = 100000;

	private static final int LOG_VIEW_REFRESHING_TIME_MIN = 1000;
	private static final int LOG_VIEW_REFRESHING_TIME_MAX = 60000;

	public GeneralPreferencePage() {
		super(GRID);
		setPreferenceStore(JlvActivator.getDefault().getPreferenceStore());
		setDescription("General settings:");
	}

	@Override
	public void init(IWorkbench workbench) {
		// initialization moved in constructor
	}

	@Override
	protected void createFieldEditors() {
		IntegerFieldEditor serverPortNumberPrefEditor = new IntegerFieldEditor(
				PreferenceManager.JLV_SERVER_PORT_NUMBER,
				"Port number:", getFieldEditorParent());
		addField(serverPortNumberPrefEditor);

		BooleanFieldEditor serverAutoStartPrefEditor = new BooleanFieldEditor(
				PreferenceManager.JLV_SERVER_AUTO_START,
				"Automatic start", getFieldEditorParent());
		addField(serverAutoStartPrefEditor);

		IntegerFieldEditor logViewBufferSizePrefEditor = new IntegerFieldEditor(
				PreferenceManager.JLV_LOGVIEW_BUFFER_SIZE,
				"Buffer size (logs):", getFieldEditorParent());
		logViewBufferSizePrefEditor.setValidRange(LOG_VIEW_BUFFER_SIZE_MIN, LOG_VIEW_BUFFER_SIZE_MAX);
		addField(logViewBufferSizePrefEditor);

		IntegerFieldEditor logViewRefreshingTimePrefEditor = new IntegerFieldEditor(
				PreferenceManager.JLV_LOGVIEW_REFRESHING_TIME,
				"Refreshing time (ms):", getFieldEditorParent());
		logViewRefreshingTimePrefEditor.setValidRange(LOG_VIEW_REFRESHING_TIME_MIN, LOG_VIEW_REFRESHING_TIME_MAX);
		addField(logViewRefreshingTimePrefEditor);
	}
}
