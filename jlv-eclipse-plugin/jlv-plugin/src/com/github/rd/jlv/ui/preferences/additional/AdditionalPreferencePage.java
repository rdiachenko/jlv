package com.github.rd.jlv.ui.preferences.additional;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.ui.preferences.PreferenceManager;
import com.github.rd.jlv.ui.preferences.PreferencePageUtils;

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

		Group logListTableSettingsGroup = PreferencePageUtils.createSettingsGroup(parent,
				"Log list view table columns");
		FieldEditor logListViewTableEditor = new LogListViewTableEditor(
				PreferenceManager.LOGS_TABLE_STRUCTURE_SETTINGS,
				PreferencePageUtils.createFieldEditorComposite(logListTableSettingsGroup));
		addField(logListViewTableEditor);

		Group logListDisplaySettingGroup = PreferencePageUtils.createSettingsGroup(parent,
				"Log list view log display");
		FieldEditor logListViewDisplayEditor = new LogListViewDisplayEditor(
				PreferenceManager.LOGS_TABLE_PRESENTATION_SETTINGS,
				PreferencePageUtils.createFieldEditorComposite(logListDisplaySettingGroup));
		addField(logListViewDisplayEditor);
	}
}
