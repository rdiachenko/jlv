package com.rdiachenko.jlv.plugin.view;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.plugin.LogField;
import com.rdiachenko.jlv.plugin.LogLevel;
import com.rdiachenko.jlv.plugin.PreferenceStoreUtils;
import com.rdiachenko.jlv.plugin.ResourceManager;
import com.rdiachenko.jlv.plugin.preference.PresentationalModel;
import com.rdiachenko.jlv.plugin.preference.PresentationalModelItem;

public class DefaultColumnLabelProvider extends ColumnLabelProvider {
    
    protected final TableViewer viewer;
    protected final LogField field;
    
    public DefaultColumnLabelProvider(TableViewer viewer, LogField field) {
        this.viewer = viewer;
        this.field = field;
    }
    
    @Override
    public String getText(Object element) {
        Log log = (Log) element;
        return field.valueOf(log);
    }

    @Override
    public Color getForeground(Object element) {
        Log log = (Log) element;
        PresentationalModel model = PreferenceStoreUtils.getPresentationalModel();
        PresentationalModelItem item = model.getModelItem(LogLevel.toLogLevel(log.getLevel()));
        return ResourceManager.getColor(Display.getCurrent(), item.getForeground());
    }
    
    @Override
    public Color getBackground(Object element) {
        Log log = (Log) element;
        PresentationalModel model = PreferenceStoreUtils.getPresentationalModel();
        PresentationalModelItem item = model.getModelItem(LogLevel.toLogLevel(log.getLevel()));
        return ResourceManager.getColor(Display.getCurrent(), item.getBackground());
    }
    
    @Override
    public Font getFont(Object element) {
        PresentationalModel model = PreferenceStoreUtils.getPresentationalModel();
        return ResourceManager.getFont(viewer.getTable(), model.getFontSize(), SWT.NONE);
    }
}
