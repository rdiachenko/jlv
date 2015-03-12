package com.github.rd.jlv.eclipse.ui.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.github.rd.jlv.CircularBuffer;
import com.github.rd.jlv.Log;

public class LoglistContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
		// no code
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// no code
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		CircularBuffer<Log> logs = (CircularBuffer<Log>) inputElement;
		return logs.toArray();
	}
}
