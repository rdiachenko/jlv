package com.rdiachenko.jlv.plugin.view;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.plugin.LogField;

public class DefaultColumnLabelProvider extends ColumnLabelProvider {

    private LogField field;

    public DefaultColumnLabelProvider(LogField field) {
        this.field = field;
    }

    @Override
    public String getText(Object element) {
        Log log = (Log) element;
        return field.valueOf(log);
    }
}
