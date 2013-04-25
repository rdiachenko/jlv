package com.rdiachenko.jlv.ui.views;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogContainer;

public class JlvViewContentProvider implements IStructuredContentProvider {

	private TableViewer viewer;

	private LogContainer logContainer;

	@Override
	public void dispose() {
		// nothing to dispose
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TableViewer) viewer;
		logContainer = (LogContainer) newInput;
//		if (manager != null)
//			manager.removeFavoritesManagerListener(this);
//		manager = (FavoritesManager) newInput;
//		if (manager != null)
//			manager.addFavoritesManagerListener(this);

	}

	@Override
	public Object[] getElements(Object inputElement) {
		Iterator<Log> iterator = logContainer.iterator();
		Object[] elements = new Object[logContainer.size()];

		for (int i = 0; iterator.hasNext(); i++) {
			elements[i] = iterator.next();
		}
		return elements;
	}

//	public void favoritesChanged(FavoritesManagerEvent event) {
//		viewer.getTable().setRedraw(false);
//		try {
//			viewer.remove(event.getItemsRemoved());
//			viewer.add(event.getItemsAdded());
//		} finally {
//			viewer.getTable().setRedraw(true);
//		}
//	}

}
