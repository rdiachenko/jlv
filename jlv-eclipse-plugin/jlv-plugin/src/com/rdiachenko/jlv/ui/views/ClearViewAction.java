package com.rdiachenko.jlv.ui.views;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

public class ClearViewAction implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

//	private JlvView view;
//
//	@Override
//	public void run(IAction action) {
//		view.clear();
//	}
//
//	@Override
//	public void selectionChanged(IAction action, ISelection selection) {
//		// no need to use this event
//	}
//
//	@Override
//	public void init(IViewPart view) {
//		this.view = (JlvView) view;
//	}
}
