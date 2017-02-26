package com.rdiachenko.jlv.plugin.preference;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.rdiachenko.jlv.plugin.JlvActivator;
import com.rdiachenko.jlv.plugin.JlvConstants;

public class GeneralPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    
    private static final int LOGLIST_VIEW_LOGS_COUNT_MIN = 50;
    private static final int LOGLIST_VIEW_LOGS_COUNT_MAX = 100000;

    private static final int LOGLIST_VIEW_REFRESHING_TIME_MS_MIN = 500;
    private static final int LOGLIST_VIEW_REFRESHING_TIME_MS_MAX = 60000;
    
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
        Composite groupComposite = createGroupComposite(serverSettingsGroup);
        Composite textFieldEditorComposite = createTextFieldEditorComposite(groupComposite);

        IntegerFieldEditor serverPortEditor = new IntegerFieldEditor(
                JlvConstants.SERVER_PORT_PREF_KEY, "Port number:", textFieldEditorComposite);
        addField(serverPortEditor);

        BooleanFieldEditor serverAutoStartEditor = new BooleanFieldEditor(
                JlvConstants.SERVER_AUTO_START_PREF_KEY, "Automatic start", groupComposite);
        addField(serverAutoStartEditor);
        
        Group logListViewSettingsGroup = PreferencePageUtils.createSettingsGroup(parent, "Loglist view settings");
        groupComposite = createGroupComposite(logListViewSettingsGroup);
        textFieldEditorComposite = createTextFieldEditorComposite(groupComposite);

        IntegerFieldEditor logListSizeEditor = new IntegerFieldEditor(
                JlvConstants.LOGLIST_BUFFER_SIZE_PREF_KEY, "Max logs count:", textFieldEditorComposite);
        logListSizeEditor.setValidRange(LOGLIST_VIEW_LOGS_COUNT_MIN, LOGLIST_VIEW_LOGS_COUNT_MAX);
        addField(logListSizeEditor);

        IntegerFieldEditor refreshTimeEditor = new IntegerFieldEditor(
                JlvConstants.LOGLIST_REFRESH_TIME_MS_PREF_KEY, "Refreshing time (ms):", textFieldEditorComposite);
        refreshTimeEditor.setValidRange(LOGLIST_VIEW_REFRESHING_TIME_MS_MIN, LOGLIST_VIEW_REFRESHING_TIME_MS_MAX);
        addField(refreshTimeEditor);

        BooleanFieldEditor quickSearchEditor = new BooleanFieldEditor(
                JlvConstants.QUICK_SEARCH_VISIBLE_PREF_KEY, "Quick search", groupComposite);
        addField(quickSearchEditor);
    }
    
    private static Composite createGroupComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.FILL);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
        GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        composite.setLayoutData(layoutData);
        return composite;
    }

    private static Composite createTextFieldEditorComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.FILL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        composite.setLayoutData(layoutData);
        return composite;
    }
}
