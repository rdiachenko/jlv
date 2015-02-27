package com.github.rd.jlv.eclipse.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.props.JlvProperties;
import com.github.rd.jlv.props.PropertyKey;

public class GeneralPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final int LOGLIST_BUFFER_SIZE_MIN = 10; // log's number
	private static final int LOGLIST_BUFFER_SIZE_MAX = 1000000; // log's number

	private static final int LOGLIST_REFRESH_TIME_MIN = 500; // ms
	private static final int LOGLIST_REFRESH_TIME_MAX = 60000; // ms

	private JlvProperties store;

	@Override
	public void init(IWorkbench workbench) {
		store = JlvActivator.getDefault().getStore();
		setPreferenceStore(JlvActivator.getDefault().getPreferenceStore());
		setDescription("General settings");
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		parent.setLayout(PreferencePageUtils.createFieldEditorParentLayout());

		Composite composite = PreferencePageUtils.createCompositeGroup(parent, "Server settings");
		addField(new IntegerFieldEditor(store, PropertyKey.SERVER_PORT_KEY, composite, "Server port:"));
		addField(new BooleanFieldEditor(store, PropertyKey.SERVER_AUTOSTART_KEY, composite, "Automatic start"));

		composite = PreferencePageUtils.createCompositeGroup(parent, "Loglist settings");
		addField(new IntegerFieldEditor(store, PropertyKey.LOGLIST_BUFFER_SIZE_KEY, composite,
				"Buffer size (logs number):", LOGLIST_BUFFER_SIZE_MIN, LOGLIST_BUFFER_SIZE_MAX));
		addField(new IntegerFieldEditor(store, PropertyKey.LOGLIST_REFRESH_TIME_KEY, composite,
				"Refreshing time (ms):", LOGLIST_REFRESH_TIME_MIN, LOGLIST_REFRESH_TIME_MAX));
		addField(new BooleanFieldEditor(store, PropertyKey.LOGLIST_QUICK_SEARCH_KEY, composite, "Quick search"));
	}
}
