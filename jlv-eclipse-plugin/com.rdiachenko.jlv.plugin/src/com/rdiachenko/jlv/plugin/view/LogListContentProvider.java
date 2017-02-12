package com.rdiachenko.jlv.plugin.view;

import org.eclipse.jface.viewers.IStructuredContentProvider;

import com.rdiachenko.jlv.CircularBuffer;
import com.rdiachenko.jlv.Log;

public class LogListContentProvider implements IStructuredContentProvider {
    
    @Override
    public Object[] getElements(Object inputElement) {
        @SuppressWarnings("unchecked")
        CircularBuffer<Log> logs = (CircularBuffer<Log>) inputElement;
        return logs.toArray();
    }
}
