package com.github.rd.jlv.eclipse.ui.preferences.additional;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.github.rd.jlv.eclipse.ui.preferences.JlvPreferencePage;
import com.github.rd.jlv.eclipse.ui.preferences.PreferencePageUtils;

public class AdditionalPreferencePage extends JlvPreferencePage {

	@Override
	public Control createContents(Composite parent) {
		parent.setLayout(PreferencePageUtils.createFieldEditorParentLayout());

		Composite composite = PreferencePageUtils.createCompositeGroup(parent, "Loglist column settings");
		// addField();

		composite = PreferencePageUtils.createCompositeGroup(parent, "Loglist level color settings");
		// addField();
		
		return parent;
	}
}
