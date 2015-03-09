package com.github.rd.jlv.eclipse.ui.preferences.additional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.github.rd.jlv.eclipse.ui.preferences.BooleanFieldEditor;
import com.github.rd.jlv.eclipse.ui.preferences.JlvPreferencePage;
import com.github.rd.jlv.eclipse.ui.preferences.PreferencePageUtils;
import com.github.rd.jlv.eclipse.ui.preferences.SpinnerFieldEditor;
import com.github.rd.jlv.props.PropertyKey;

public class AdditionalPreferencePage extends JlvPreferencePage {

	@Override
	public Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(PreferencePageUtils.createFieldEditorParentLayout());

		Composite group = PreferencePageUtils.createCompositeGroup(composite, "Loglist column settings");
		addField(new LoglistColumnFieldEditor(PropertyKey.LOGLIST_COLUMN_KEY, group));

		group = PreferencePageUtils.createCompositeGroup(composite, "Loglist level color settings");
		addField(new BooleanFieldEditor(PropertyKey.LOGLIST_LEVEL_IMAGE_KEY, "Use image to display log level", group));
		addField(new SpinnerFieldEditor(PropertyKey.LOGLIST_FONT_SIZE_KEY, "Font size", group));

		return composite;
	}
}
