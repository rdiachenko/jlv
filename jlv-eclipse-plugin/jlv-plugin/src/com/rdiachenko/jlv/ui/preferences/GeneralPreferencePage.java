package com.rdiachenko.jlv.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.rdiachenko.jlv.JlvActivator;

public class GeneralPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

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
	}
}
