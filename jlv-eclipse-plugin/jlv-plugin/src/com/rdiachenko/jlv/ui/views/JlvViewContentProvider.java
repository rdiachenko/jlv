package com.rdiachenko.jlv.ui.views;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogContainer;

public class JlvViewContentProvider implements IStructuredContentProvider {

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
			elements = new Object[logContainer.size()];
			Iterator<Log> iter = logContainer.iterator();

			for (int i = 0; iter.hasNext(); i++) {
				elements[i] = iter.next();
			}
		}
		return elements;
	}
}
