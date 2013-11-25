package com.github.rd.jlv.ui.views;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.log4j.domain.LogContainer;

public class LogListViewContentProvider implements IStructuredContentProvider {

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
		Object[] elements = new Object[0];

		if (inputElement instanceof LogContainer) {
			LogContainer logContainer = (LogContainer) inputElement;
			Iterator<Log> iter = logContainer.iterator();
			elements = new Object[logContainer.size()];

			for (int i = 0; i < elements.length && iter.hasNext(); i++) {
				elements[i] = iter.next();
			}
		}
		return elements;
	}
}
