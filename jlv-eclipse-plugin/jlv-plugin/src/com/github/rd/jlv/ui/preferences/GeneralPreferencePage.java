package com.github.rd.jlv.ui.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.pfers.PreferenceEnum;

public class GeneralPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(JlvActivator.getDefault().getPreferenceStore());
		setDescription("General settings");
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		parent.setLayout(PreferencePageUtils.createFieldEditorParentLayout());
		
		FieldEditor generalPreferenceEditor = new GeneralPreferenceEditor(
				PreferenceEnum.JLV_GENERAL_SETTINGS.getName(),
				parent);
		addField(generalPreferenceEditor);
	}
}
