package com.github.rd.jlv.eclipse.ui.preferences.additional;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.eclipse.ui.preferences.PreferencePageUtils;
import com.github.rd.jlv.prefs.PreferenceEnum;

public class AdditionalPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(JlvActivator.getDefault().getPreferenceStore());
		setDescription("Additional settings");
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		parent.setLayout(PreferencePageUtils.createFieldEditorParentLayout());

		Group structuralSettingsGroup = PreferencePageUtils.createSettingsGroup(parent,
				"Structural log table settings");
		FieldEditor structuralTableEditor = new StructuralTableEditor(
				PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS.getName(),
				PreferencePageUtils.createFieldEditorComposite(structuralSettingsGroup));
		addField(structuralTableEditor);

		Group presentationalSettingGroup = PreferencePageUtils.createSettingsGroup(parent,
				"Presentational log table settings");
		FieldEditor presentationalTableEditor = new PresentationalTableEditor(
				PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS.getName(),
				PreferencePageUtils.createFieldEditorComposite(presentationalSettingGroup));
		addField(presentationalTableEditor);
	}
}
