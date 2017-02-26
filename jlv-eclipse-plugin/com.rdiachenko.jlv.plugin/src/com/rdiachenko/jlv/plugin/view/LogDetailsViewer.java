package com.rdiachenko.jlv.plugin.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.plugin.LogField;
import com.rdiachenko.jlv.plugin.ResourceManager;

public class LogDetailsViewer {
    
    private static final String COLON = ":";
    
    private final StyledText viewer;
    private final Font logFieldNameFont;
    
    public LogDetailsViewer(Composite parent) {
        viewer = new StyledText(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        viewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        viewer.setEditable(false);
        logFieldNameFont = ResourceManager.getFont(viewer, 11, SWT.BOLD);
    }
    
    public void clear() {
        viewer.setText("");
    }
    
    public void showDetails(Log log) {
        List<StyleRange> styles = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        
        for (LogField field : LogField.values()) {
            String value = field.valueOf(log);
            
            if (!value.isEmpty()) {
                StyleRange style = new StyleRange();
                style.start = builder.length();
                style.length = field.getName().length() + COLON.length();
                style.font = logFieldNameFont;
                style.foreground = viewer.getDisplay().getSystemColor(SWT.COLOR_GRAY);
                styles.add(style);
                
                builder.append(field.getName().toUpperCase())
                        .append(COLON)
                        .append(System.lineSeparator())
                        .append(value)
                        .append(System.lineSeparator());
            }
        }
        viewer.setText(builder.toString().trim());
        
        for (StyleRange style : styles) {
            viewer.setStyleRange(style);
        }
    }
}
