package com.github.rd.jlv.eclipse.ui.views;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.eclipse.StringConstants;
import com.github.rd.jlv.props.JlvProperties;
import com.github.rd.jlv.props.PropertyKey;

/**
 * Represents a handler for loglist view life cycle actions: close/open view, activate/deactivate view, etc.
 * 
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public class ViewLifecycleListener implements IPartListener {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void partActivated(IWorkbenchPart part) {
		// no code
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		// no code
	}

	@Override
	public void partClosed(final IWorkbenchPart part) {
		if (StringConstants.JLV_PLUGIN_ID.equals(part.getSite().getPluginId())) {
			if (part instanceof LoglistView) {
				logger.debug("Jlv loglist view closed.");
			}
		}
	}

	@Override
	public void partDeactivated(final IWorkbenchPart part) {
		// no code
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		if (StringConstants.JLV_PLUGIN_ID.equals(part.getSite().getPluginId())) {
			if (part instanceof LoglistView) {
				JlvProperties store = JlvActivator.getDefault().getStore();
				boolean isServerAutoStart = store.load(PropertyKey.SERVER_AUTOSTART_KEY);
				logger.debug("Server auto start option: {}", isServerAutoStart);

				if (isServerAutoStart) {
					LoglistView view = (LoglistView) part;
					view.startServer();
				}
				logger.debug("Jlv loglist view opened.");
			}
		}
	}
}