package com.rdiachenko.jlv.plugin.preference;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Layout;

public final class PreferencePageUtils {

    private PreferencePageUtils() {
        // Utility class
    }
    
    public static Layout createFieldEditorParentLayout() {
        GridLayout layout = new GridLayout();
        layout.verticalSpacing = 15;
        return layout;
    }
    
    public static Group createSettingsGroup(Composite parent, String groupName) {
        Group group = new Group(parent, SWT.NONE);
        group.setText(groupName);
        GridLayout layout = new GridLayout();
        group.setLayout(layout);
        GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        group.setLayoutData(layoutData);
        return group;
    }
}
