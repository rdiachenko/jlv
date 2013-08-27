package com.rdiachenko.jlv.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import com.rdiachenko.jlv.JlvActivator;

public final class PreferenceManager {

	public static final String JLV_SERVER_PORT_NUMBER = "jlv.server.port.number";
	public static final String JLV_SERVER_AUTO_START = "jlv.server.auto.start";

	public static final String JLV_LOG_LIST_VIEW_QUICK_SEARCH_FIELD_STATUS = "jlv.loglistview.quick.search.field.visible";

	public static final String JLV_LOG_LIST_VIEW_BUFFER_SIZE = "jlv.loglistview.buffer.size";
	public static final String JLV_LOG_LIST_VIEW_REFRESHING_TIME = "jlv.loglistview.refreshing.time";

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

	public static void setLogListViewQuickSearchFieldStatus(boolean isVisible) {
		STORE.setValue(JLV_LOG_LIST_VIEW_QUICK_SEARCH_FIELD_STATUS, isVisible);
	}

	public static boolean getLogListViewQuickSearchFieldStatus() {
		return STORE.getBoolean(JLV_LOG_LIST_VIEW_QUICK_SEARCH_FIELD_STATUS);
	}

	public static int getLogListViewBufferSize() {
		return STORE.getInt(JLV_LOG_LIST_VIEW_BUFFER_SIZE);
	}

	public static int getLogListViewRefreshingTime() {
		return STORE.getInt(JLV_LOG_LIST_VIEW_REFRESHING_TIME);
	}
}
