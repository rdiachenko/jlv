package com.rdiachenko.jlv.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import com.rdiachenko.jlv.JlvActivator;

public final class PreferenceManager {

	public static final String JLV_SERVER_PORT_NUMBER = "jlv.server.port.number";
	public static final String JLV_SERVER_AUTO_START = "jlv.server.auto.start";

	public static final String JLV_QUICK_SEARCH_FIELD_STATUS = "jlv.logview.main.quick.search.field.visible";

	public static final String JLV_LOGVIEW_BUFFER_SIZE = "jlv.logview.main.buffer.size";
	public static final String JLV_LOGVIEW_REFRESHING_TIME = "jlv.logview.main.refreshing.time";

	private static final IPreferenceStore STORE = JlvActivator.getDefault().getPreferenceStore();

	private PreferenceManager() {
		throw new IllegalStateException("This is an util class. The object should not be created.");
	}

	public static int getServerPortNumber() {
		return STORE.getInt(JLV_SERVER_PORT_NUMBER);
	}

	public static boolean getServerAutoStart() {
		return STORE.getBoolean(JLV_SERVER_AUTO_START);
	}

	public static void setQuickSearchFieldStatus(boolean isVisible) {
		STORE.setValue(JLV_QUICK_SEARCH_FIELD_STATUS, isVisible);
	}

	public static boolean getQuickSearchFieldStatus() {
		return STORE.getBoolean(JLV_QUICK_SEARCH_FIELD_STATUS);
	}

	public static int getLogViewBufferSize() {
		return STORE.getInt(JLV_LOGVIEW_BUFFER_SIZE);
	}

	public static int getLogViewRefreshingTime() {
		return STORE.getInt(JLV_LOGVIEW_REFRESHING_TIME);
	}
}
