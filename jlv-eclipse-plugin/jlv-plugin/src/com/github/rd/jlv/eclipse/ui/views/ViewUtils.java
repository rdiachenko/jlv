package com.github.rd.jlv.eclipse.ui.views;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public final class ViewUtils {

	public static IViewPart openView(String viewId) {
		IViewPart view = null;

		try {
			IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			view = workbenchPage.findView(viewId);

			if (view == null) {
				view = workbenchPage.showView(viewId);
			}
		} catch (PartInitException e) {
			throw new IllegalStateException(viewId + " view couldn't be initialized:", e);
		}
		return view;
	}

	private ViewUtils() {
		throw new IllegalStateException("This is an util class. The object should not be created.");
	}
}
