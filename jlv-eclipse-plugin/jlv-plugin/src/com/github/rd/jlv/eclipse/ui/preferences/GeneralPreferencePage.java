package com.github.rd.jlv.eclipse.ui.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.github.rd.jlv.props.PropertyKey;

public class GeneralPreferencePage extends JlvPreferencePage {

	private static final int LOGLIST_BUFFER_SIZE_MIN = 10; // logs number
	private static final int LOGLIST_BUFFER_SIZE_MAX = 1000000; // logs number

	private static final int LOGLIST_REFRESH_TIME_MIN = 500; // ms
	private static final int LOGLIST_REFRESH_TIME_MAX = 60000; // ms

	@Override
	public Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(PreferencePageUtils.createFieldEditorParentLayout());

		Composite group = PreferencePageUtils.createCompositeGroup(composite, "Server settings");
		addField(new IntegerFieldEditor(PropertyKey.SERVER_PORT_KEY, "Server port:", group));
		addField(new BooleanFieldEditor(PropertyKey.SERVER_AUTOSTART_KEY, "Automatic start", group));

		group = PreferencePageUtils.createCompositeGroup(composite, "Loglist settings");
		addField(new IntegerFieldEditor(PropertyKey.LOGLIST_BUFFER_SIZE_KEY, "Buffer size (logs number):",
				group, LOGLIST_BUFFER_SIZE_MIN, LOGLIST_BUFFER_SIZE_MAX));
		addField(new IntegerFieldEditor(PropertyKey.LOGLIST_REFRESH_TIME_KEY, "Refreshing time (ms):",
				group, LOGLIST_REFRESH_TIME_MIN, LOGLIST_REFRESH_TIME_MAX));
		addField(new BooleanFieldEditor(PropertyKey.LOGLIST_QUICK_SEARCH_KEY, "Quick search", group));

		return composite;
	}
}
