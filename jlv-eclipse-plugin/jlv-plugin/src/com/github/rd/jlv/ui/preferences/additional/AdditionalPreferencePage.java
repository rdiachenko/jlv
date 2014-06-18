package com.github.rd.jlv.ui.preferences.additional;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.pfers.PreferenceEnum;
import com.github.rd.jlv.ui.preferences.PreferencePageUtility;

public class AdditionalPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(JlvActivator.getDefault().getPreferenceStore());
		setDescription("Additional settings");
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		parent.setLayout(PreferencePageUtility.createFieldEditorParentLayout());

		Group structuralSettingsGroup = PreferencePageUtility.createSettingsGroup(parent,
				"Structural log table settings");
		FieldEditor structuralTableEditor = new StructuralTableEditor(
				PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS.getName(),
				PreferencePageUtility.createFieldEditorComposite(structuralSettingsGroup));
		addField(structuralTableEditor);

		Group presentationalSettingGroup = PreferencePageUtility.createSettingsGroup(parent,
				"Presentational log table settings");
		FieldEditor presentationalTableEditor = new PresentationalTableEditor(
				PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS.getName(),
				PreferencePageUtility.createFieldEditorComposite(presentationalSettingGroup));
		addField(presentationalTableEditor);
	}
}
