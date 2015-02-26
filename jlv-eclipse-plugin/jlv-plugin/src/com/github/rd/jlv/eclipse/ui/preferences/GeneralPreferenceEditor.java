package com.github.rd.jlv.eclipse.ui.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.prefs.Converter;
import com.github.rd.jlv.prefs.GeneralModel;
import com.github.rd.jlv.prefs.PreferenceEnum;

public class GeneralPreferenceEditor extends FieldEditor {

	private static final int LOG_LIST_VIEW_BUFFER_SIZE_MIN = 50; // log's number
	private static final int LOG_LIST_VIEW_BUFFER_SIZE_MAX = 100000; // log's number

	private static final int LOG_LIST_VIEW_REFRESHING_TIME_MIN = 500; // ms
	private static final int LOG_LIST_VIEW_REFRESHING_TIME_MAX = 60000; // ms

	private static final PreferenceEnum TYPE = PreferenceEnum.JLV_GENERAL_SETTINGS;
	private static final Converter CONVERTER = Converter.get(TYPE);

	private GeneralModel model;

	private Composite topComposite;

	private Text serverPortFieldControl;
	private Button autoStartServerSwitcherControl;

	private Text bufferSizeFieldControl;
	private Text refreshTimeFieldControl;
	private Button quickSearchSwitcherControl;

	private PreferenceManager preferenceManager;

	public GeneralPreferenceEditor(String name, Composite parent) {
		init(name, "");
		preferenceManager = JlvActivator.getDefault().getPreferenceManager();
		createControl(parent);
	}

	@Override
	public void adjustForNumColumns(int numColumns) {
		((GridData) topComposite.getLayoutData()).horizontalSpan = numColumns;
	}

	@Override
	public int getNumberOfControls() {
		return 1; // one control per line in the layout grid
	}

	@Override
	public void doFillIntoGrid(Composite parent, int numColumns) {
		topComposite = parent;

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
		model = (GeneralModel) CONVERTER.jsonToModel(getPreferenceStore().getString(TYPE.getName()));
		init();
	}

	@Override
	public void doLoadDefault() {
		model = (GeneralModel) CONVERTER.getDefaultModel();
		init();
	}

	@Override
	public void doStore() {
		preferenceManager.setValue(PreferenceEnum.JLV_GENERAL_SETTINGS, model);
	}

	private void init() {
		serverPortFieldControl.setText(String.valueOf(model.getPortNumber()));
		autoStartServerSwitcherControl.setSelection(model.isAutoStart());
		bufferSizeFieldControl.setText(String.valueOf(model.getBufferSize()));
		refreshTimeFieldControl.setText(String.valueOf(model.getRefreshingTime()));
		quickSearchSwitcherControl.setSelection(model.isQuickSearch());
	}

	private void createServerPortFieldControl(Composite parent) {
		serverPortFieldControl = PreferencePageUtils.createTextFieldControl(parent, "Port number:");
		final ValueHandler valueHandler = new ValueHandler() {
			@Override
			protected void valueChanged() {
				model.setPortNumber(Integer.parseInt(serverPortFieldControl.getText()));
			}
		};
		addListeners(serverPortFieldControl, valueHandler);
	}

	private void createAutoStartServerSwitcherControl(Composite parent) {
		autoStartServerSwitcherControl = PreferencePageUtils.createCheckBoxControl(parent, "Automatic start");
		autoStartServerSwitcherControl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.setAutoStart(autoStartServerSwitcherControl.getSelection());
			}
		});
	}

	private void createBufferSizeFieldControl(Composite parent) {
		bufferSizeFieldControl = PreferencePageUtils.createTextFieldControl(parent, "Buffer size (logs):");
		final ValueHandler valueHandler = new ValueHandler() {
			@Override
			protected void valueChanged() {
				int value = Integer.parseInt(bufferSizeFieldControl.getText());

				if (value >= LOG_LIST_VIEW_BUFFER_SIZE_MIN && value <= LOG_LIST_VIEW_BUFFER_SIZE_MAX) {
					model.setBufferSize(value);
					clearErrorMessage();
				} else {
					showErrorMessage("Buffer size is out of the [" + LOG_LIST_VIEW_BUFFER_SIZE_MIN + ","
							+ LOG_LIST_VIEW_BUFFER_SIZE_MAX + "] range.");
				}
			}
		};
		addListeners(bufferSizeFieldControl, valueHandler);
	}

	private void createRefreshTimeFieldControl(Composite parent) {
		refreshTimeFieldControl = PreferencePageUtils.createTextFieldControl(parent, "Refreshing time (ms):");
		final ValueHandler valueHandler = new ValueHandler() {
			@Override
			protected void valueChanged() {
				int value = Integer.parseInt(refreshTimeFieldControl.getText());

				if (value >= LOG_LIST_VIEW_REFRESHING_TIME_MIN && value <= LOG_LIST_VIEW_REFRESHING_TIME_MAX) {
					model.setRefreshingTime(value);
					clearErrorMessage();
				} else {
					showErrorMessage("Refreshing time is out of the [" + LOG_LIST_VIEW_REFRESHING_TIME_MIN + ","
							+ LOG_LIST_VIEW_REFRESHING_TIME_MAX + "] range.");
				}
			}
		};
		addListeners(refreshTimeFieldControl, valueHandler);
	}

	private void createQuickSearchSwitcherControl(Composite parent) {
		quickSearchSwitcherControl = PreferencePageUtils.createCheckBoxControl(parent, "Quick search");
		quickSearchSwitcherControl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.setQuickSearch(quickSearchSwitcherControl.getSelection());
			}
		});
	}

	private void addListeners(final Text field, final ValueHandler valueHandler) {
		field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				valueHandler.valueChanged();
			}
		});
		field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				valueHandler.valueChanged();
			}
		});
		field.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(final VerifyEvent e) {
				e.doit = valueHandler.isValid(e.text);
			}
		});
	}

	private abstract class ValueHandler {

		protected abstract void valueChanged();

		protected boolean isValid(String value) {
			return value.matches("[\\d]*");
		}
	}
}
