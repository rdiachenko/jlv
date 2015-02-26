package com.github.rd.jlv.eclipse.ui.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.github.rd.jlv.log4j.domain.LogCollection;

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
		LogCollection logs = (LogCollection) inputElement;
		return logs.toArray();
	}
}
