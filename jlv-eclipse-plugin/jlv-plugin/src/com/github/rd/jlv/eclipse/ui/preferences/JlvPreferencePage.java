package com.github.rd.jlv.eclipse.ui.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.props.JlvProperties;

public abstract class JlvPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private JlvProperties store;
	private List<FieldEditor> fieldEditors;

	@Override
	public void init(IWorkbench workbench) {
		store = JlvActivator.getDefault().getStore();
		fieldEditors = new ArrayList<>();
	}

	@Override
	public boolean performOk() {
		boolean state = false;

		try {
			for (FieldEditor field : fieldEditors) {
				field.save();
			}
			store.persist();
		} finally {
			state = super.performOk();
		}
		return state;
	}

	@Override
	public void performDefaults() {
		for (FieldEditor field : fieldEditors) {
			field.loadDefault();
		}
	}

	@Override
	public void dispose() {
		super.dispose();

		for (FieldEditor field : fieldEditors) {
			field.dispose();
		}
	}

	public void addField(FieldEditor fieldEditor) {
		fieldEditor.setPage(this);
		fieldEditor.setStore(store);
		fieldEditor.load();
		fieldEditors.add(fieldEditor);
	}
}
