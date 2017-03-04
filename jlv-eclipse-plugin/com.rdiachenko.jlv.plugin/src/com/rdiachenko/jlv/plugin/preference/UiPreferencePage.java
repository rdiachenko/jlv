package com.rdiachenko.jlv.plugin.preference;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.rdiachenko.jlv.plugin.JlvActivator;

public class UiPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  @Override
  public void init(IWorkbench workbench) {
    setPreferenceStore(JlvActivator.getDefault().getPreferenceStore());
    setDescription("User iterface settings");
  }

  @Override
  protected void createFieldEditors() {
    Composite parent = getFieldEditorParent();
    parent.setLayout(PreferencePageUtils.createFieldEditorParentLayout());

    Group presentationalSettingsGroup = PreferencePageUtils.createSettingsGroup(parent,
        "Presentational log table settings");
    FieldEditor presentationalTableEditor = new PresentationalTableEditor(
        createFieldEditorComposite(presentationalSettingsGroup));
    addField(presentationalTableEditor);

    Group structuralSettingsGroup = PreferencePageUtils.createSettingsGroup(parent,
        "Structural log table settings");
    FieldEditor structuralTableEditor = new StructuralTableEditor(
        createFieldEditorComposite(structuralSettingsGroup));
    addField(structuralTableEditor);
  }

  private static Composite createFieldEditorComposite(Composite parent) {
    Composite composite = new Composite(parent, SWT.FILL);
    composite.setLayout(new GridLayout());
    GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    composite.setLayoutData(layoutData);
    return composite;
  }
}
