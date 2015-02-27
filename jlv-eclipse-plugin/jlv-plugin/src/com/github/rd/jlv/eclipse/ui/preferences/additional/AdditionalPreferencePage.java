package com.github.rd.jlv.eclipse.ui.preferences.additional;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.eclipse.ui.preferences.PreferencePageUtils;
import com.github.rd.jlv.props.JlvProperties;

public class AdditionalPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private JlvProperties store;

	@Override
	public void init(IWorkbench workbench) {
		store = JlvActivator.getDefault().getStore();
		setPreferenceStore(JlvActivator.getDefault().getPreferenceStore());
		setDescription("Additional settings");
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		parent.setLayout(PreferencePageUtils.createFieldEditorParentLayout());

		Composite composite = PreferencePageUtils.createCompositeGroup(parent, "Loglist column settings");
		// addField();

		composite = PreferencePageUtils.createCompositeGroup(parent, "Loglist level color settings");
		// addField();
	}
}
