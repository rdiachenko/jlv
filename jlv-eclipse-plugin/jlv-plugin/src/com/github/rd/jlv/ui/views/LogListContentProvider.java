package com.github.rd.jlv.ui.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.github.rd.jlv.log4j.domain.LogContainer;

public class LogListContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
		// no code
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// no code
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof LogContainer) {
			LogContainer logContainer = (LogContainer) inputElement;
			return logContainer.getLogs().toArray();
		}
		return new Object[0];
	}
}
