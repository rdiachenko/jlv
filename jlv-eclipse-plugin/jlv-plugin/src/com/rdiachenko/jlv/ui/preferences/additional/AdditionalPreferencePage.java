package com.rdiachenko.jlv.ui.preferences.additional;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.rdiachenko.jlv.JlvActivator;
import com.rdiachenko.jlv.ui.preferences.PreferenceManager;
import com.rdiachenko.jlv.ui.preferences.PreferencePageUtils;

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
		LogListViewTableEditor logListViewTableEditor = new LogListViewTableEditor(
				PreferenceManager.LOGS_TABLE_STRUCTURE_SETTINGS,
				PreferencePageUtils.createFieldEditorComposite(logListTableSettingsGroup));
		addField(logListViewTableEditor);

		Group logListDisplaySettingGroup = PreferencePageUtils.createSettingsGroup(parent,
				"Log list view log display");
		BooleanFieldEditor imageInsteadTextPrefEditor = new BooleanFieldEditor(
				PreferenceManager.IMAGE_INSTEAD_TEXT_LEVEL_STATE,
				"Use image to display log level",
				PreferencePageUtils.createFieldEditorComposite(logListDisplaySettingGroup));
		addField(imageInsteadTextPrefEditor);
		SpinnerFieldEditor logFontSizeEditor = new SpinnerFieldEditor(
				PreferenceManager.LOGS_FONT_SIZE,
				"Log's font size",
				PreferencePageUtils.createFieldEditorComposite(logListDisplaySettingGroup));
		addField(logFontSizeEditor);
		LogListViewDisplayEditor logListViewDisplayEditor = new LogListViewDisplayEditor(
				PreferenceManager.LOGS_TABLE_PRESENTATION_SETTINGS,
				PreferencePageUtils.createFieldEditorComposite(logListDisplaySettingGroup));
		addField(logListViewDisplayEditor);
	}
}
