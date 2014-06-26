package com.github.rd.jlv.ui.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.pfers.GeneralModel;

public class GeneralPreferenceEditor extends FieldEditor {

	private GeneralModel model;
	
	private PreferenceManager preferenceManager;
	
	private Text serverPortFieldControl;
	private Button autoStartServerSwitcherControl;
	
	private Text bufferSizeFieldControl;
	private Text refreshTimeFieldControl;
	private Button quickSearchSwitcherControl;
	
	public GeneralPreferenceEditor(String name, Composite parent) {
		init(name, "");
		preferenceManager = JlvActivator.getDefault().getPreferenceManager();
		model = preferenceManager.getDefaultGeneralPrefs();
		createControl(parent);
	}
	
	@Override
	public void adjustForNumColumns(int numColumns) {
		// TODO: see
	}

	@Override
	public int getNumberOfControls() {
		return 1; // one group per line
	}

	@Override
	public void doFillIntoGrid(Composite parent, int numColumns) {
		Group serverSettingsGroup = PreferencePageUtils.createSettingsGroup(parent, "Server settings");
		Composite groupComposite = PreferencePageUtils.createFieldEditorComposite(serverSettingsGroup);
		createServerPortFieldControl(groupComposite);
		createAutoStartServerSwitcherControl(groupComposite);
		
		Group logListViewSettingsGroup = PreferencePageUtils.createSettingsGroup(parent,
				"Log list view settings");
		groupComposite = PreferencePageUtils.createFieldEditorComposite(logListViewSettingsGroup);
		createBufferSizeFieldControl(groupComposite);
		createRefreshTimeFieldControl(groupComposite);
		createQuickSearchSwitcherControl(groupComposite);
	}

	@Override
	public void doLoad() {
		doLoad(preferenceManager.getGeneralPrefs());
	}

	@Override
	public void doLoadDefault() {
		doLoad(preferenceManager.getDefaultGeneralPrefs());
	}

	@Override
	public void doStore() {
		// TODO: check for null here or in preference manager
		preferenceManager.storeGeneralPrefs(model);
	}

	private void doLoad(GeneralModel model) {
		this.model = model;
		
		if (serverPortFieldControl != null) {
			serverPortFieldControl.setText(String.valueOf(this.model.getPortNumber()));
		}
		
		if (autoStartServerSwitcherControl != null) {
			autoStartServerSwitcherControl.setSelection(this.model.isAutoStart());
		}
		
		if (bufferSizeFieldControl != null) {
			bufferSizeFieldControl.setText(String.valueOf(this.model.getBufferSize()));
		}
		
		if (refreshTimeFieldControl != null) {
			refreshTimeFieldControl.setText(String.valueOf(this.model.getRefreshingTime()));
		}
		
		if (quickSearchSwitcherControl != null) {
			quickSearchSwitcherControl.setSelection(this.model.isQuickSearch());
		}
	}
	
	private void createServerPortFieldControl(Composite parent) {
		if (serverPortFieldControl == null) {
			serverPortFieldControl = PreferencePageUtils.createTextFieldControl(parent, "Port number:");
			serverPortFieldControl.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					model.setPortNumber(Integer.parseInt(serverPortFieldControl.getText())); // TODO: crate integer field
				}
			});
		} else {
			checkParent(serverPortFieldControl, parent);
		}
	}
	
	private void createAutoStartServerSwitcherControl(Composite parent) {
		if (autoStartServerSwitcherControl == null) {
			autoStartServerSwitcherControl = PreferencePageUtils.createCheckBoxControl(parent, "Automatic start");
			autoStartServerSwitcherControl.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					model.setAutoStart(autoStartServerSwitcherControl.getSelection());
				}
			});
		} else {
			checkParent(autoStartServerSwitcherControl, parent);
		}
	}
	
	private void createBufferSizeFieldControl(Composite parent) {
		if (bufferSizeFieldControl == null) {
			bufferSizeFieldControl = PreferencePageUtils.createTextFieldControl(parent, "Buffer size (logs):");
			bufferSizeFieldControl.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					model.setBufferSize(Integer.parseInt(bufferSizeFieldControl.getText())); // TODO: crate integer field
				}
			});
		} else {
			checkParent(bufferSizeFieldControl, parent);
		}
	}
	
	private void createRefreshTimeFieldControl(Composite parent) {
		if (refreshTimeFieldControl == null) {
			refreshTimeFieldControl = PreferencePageUtils.createTextFieldControl(parent, "Refreshing time (ms):");
			refreshTimeFieldControl.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					model.setRefreshingTime(Integer.parseInt(refreshTimeFieldControl.getText())); // TODO: crate integer field
				}
			});
		} else {
			checkParent(refreshTimeFieldControl, parent);
		}
	}
	
	private void createQuickSearchSwitcherControl(Composite parent) {
		if (quickSearchSwitcherControl == null) {
			quickSearchSwitcherControl = PreferencePageUtils.createCheckBoxControl(parent, "Quick search");
			quickSearchSwitcherControl.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					model.setQuickSearch(quickSearchSwitcherControl.getSelection());
				}
			});
		} else {
			checkParent(quickSearchSwitcherControl, parent);
		}
	}
}
