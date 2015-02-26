package com.github.rd.jlv.eclipse.ui.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.props.JlvProperties;
import com.github.rd.jlv.props.PropertyKey;

public class GeneralPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private JlvProperties store = new JlvProperties(null);

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
		Composite groupComposite = PreferencePageUtils.createFieldEditorComposite(serverSettingsGroup);

		FieldEditor portEditor = new IntegerEditor(store, PropertyKey.SERVER_PORT_KEY, groupComposite, "Server port:");
		addField(portEditor);
	}
}
